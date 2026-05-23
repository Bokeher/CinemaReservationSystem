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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;
    private final ScreeningService screeningService;
    private final ReservationMapper reservationMapper;

    public Reservation getById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    public void cancelReservation(Long id, UserPrincipal userPrincipal) {
        Reservation reservation = getById(id);

        validateOwnership(reservation, userPrincipal);

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new ReservationAlreadyCancelledException();
        }

        reservation.setStatus(ReservationStatus.CANCELLED);

        reservationRepository.save(reservation);
    }

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
                .build();

        validateReservation(reservation);

        Reservation savedReservation = reservationRepository.save(reservation);

        return reservationMapper.toResponse(savedReservation);
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

    }

    private void validateOwnership(Reservation reservation, UserPrincipal userPrincipal) {
        if (!reservation.getUser().getId().equals(userPrincipal.getUser().getId())) {
            throw new ReservationAccessDeniedException(reservation.getId());
        }
    }

}
