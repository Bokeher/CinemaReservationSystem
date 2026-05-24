package com.bokeher.cinema.CinemaReservationSystem.reservation;

import com.bokeher.cinema.CinemaReservationSystem.reservation.dto.CreateReservationRequest;
import com.bokeher.cinema.CinemaReservationSystem.reservation.dto.ReservationResponse;
import com.bokeher.cinema.CinemaReservationSystem.reservation.exception.*;
import com.bokeher.cinema.CinemaReservationSystem.screening.Screening;
import com.bokeher.cinema.CinemaReservationSystem.screening.ScreeningService;
import com.bokeher.cinema.CinemaReservationSystem.seat.Seat;
import com.bokeher.cinema.CinemaReservationSystem.seat.exception.SeatNotFoundException;
import com.bokeher.cinema.CinemaReservationSystem.seat.SeatRepository;
import com.bokeher.cinema.CinemaReservationSystem.security.UserPrincipal;
import com.bokeher.cinema.CinemaReservationSystem.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;
    private final ScreeningService screeningService;
    private final ReservationMapper reservationMapper;

    public Reservation getById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    @Transactional
    public void cancelReservation(Long id, UserPrincipal userPrincipal) {
        Reservation reservation = getById(id);

        validateOwnership(reservation, userPrincipal);

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new ReservationAlreadyCancelledException();
        }

        // 'active=null' allows to have multiple cancelled reservations
        reservation.setActive(null);
        reservation.setStatus(ReservationStatus.CANCELLED);

        reservationRepository.save(reservation);
    }

    @Transactional
    public void confirmReservation(Long id, UserPrincipal userPrincipal) {
        Reservation reservation = getById(id);

        validateOwnership(reservation, userPrincipal);

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new ReservationCannotBeConfirmedWhenCancelledException();
        }

        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            throw new ReservationAlreadyConfirmedException();
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);

        reservationRepository.save(reservation);
    }

    @Transactional
    public ReservationResponse createReservation(CreateReservationRequest request, UserPrincipal userPrincipal) {
        Seat seat = seatRepository.findById(request.getSeatId())
                .orElseThrow(() -> new SeatNotFoundException(request.getSeatId()));

        Screening screening = screeningService.getById(request.getScreeningId());

        User user = userPrincipal.getUser();

        Reservation reservation = Reservation.builder()
                .seat(seat)
                .screening(screening)
                .status(ReservationStatus.PENDING)
                .user(user)
                .active(true)
                .build();

        validateReservation(reservation);

        try {
            Reservation savedReservation = reservationRepository.save(reservation);

            return reservationMapper.toResponse(savedReservation);
        } catch (DataIntegrityViolationException ex) {
            throw new SeatAlreadyTakenException(seat.getId());
        }
    }

    public ReservationResponse findById(Long id, UserPrincipal userPrincipal) {
        Reservation reservation = getById(id);

        validateOwnership(reservation, userPrincipal);

        return reservationMapper.toResponse(reservation);
    }

    public List<ReservationResponse> findAllForCurrentUser(UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUser().getId();

        List<Reservation> reservations = reservationRepository.findAllByUserId(userId);

        return reservations.stream()
                .map(reservationMapper::toResponse)
                .toList();
    }

    private void validateReservation(Reservation reservation) {
        Long screeningId = reservation.getScreening().getId();
        Long seatId = reservation.getSeat().getId();

        boolean existsReservation = reservationRepository.existsByScreeningIdAndSeatIdAndStatusIn(
                screeningId,
                seatId,
                List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED)
        );

        if (existsReservation) {
            throw new SeatAlreadyTakenException(seatId);
        }

        boolean isSeatValid = screeningService.isSeatValid(screeningId, seatId);

        if (!isSeatValid) {
            throw new SeatDoesNotBelongToScreeningException(seatId, screeningId);
        }
    }

    private void validateOwnership(Reservation reservation, UserPrincipal userPrincipal) {
        if (!reservation.getUser().getId().equals(userPrincipal.getUser().getId())) {
            throw new ReservationAccessDeniedException(reservation.getId());
        }
    }

}
