package com.bokeher.cinema.CinemaReservationSystem.reservation;

import com.bokeher.cinema.CinemaReservationSystem.movie.MovieMapper;
import com.bokeher.cinema.CinemaReservationSystem.reservation.dto.CreateReservationRequest;
import com.bokeher.cinema.CinemaReservationSystem.reservation.dto.ReservationResponse;
import com.bokeher.cinema.CinemaReservationSystem.reservation.exception.*;
import com.bokeher.cinema.CinemaReservationSystem.room.RoomMapper;
import com.bokeher.cinema.CinemaReservationSystem.screening.ScreeningMapper;
import com.bokeher.cinema.CinemaReservationSystem.screening.ScreeningService;
import com.bokeher.cinema.CinemaReservationSystem.screening.exception.ScreeningNotFoundException;
import com.bokeher.cinema.CinemaReservationSystem.seat.SeatMapper;
import com.bokeher.cinema.CinemaReservationSystem.seat.SeatRepository;
import com.bokeher.cinema.CinemaReservationSystem.seat.exception.SeatNotFoundException;
import com.bokeher.cinema.CinemaReservationSystem.security.UserPrincipal;
import com.bokeher.cinema.CinemaReservationSystem.user.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static com.bokeher.cinema.CinemaReservationSystem.reservation.ReservationAssertions.assertCapturedReservation;
import static com.bokeher.cinema.CinemaReservationSystem.reservation.ReservationAssertions.assertReservationResponse;
import static com.bokeher.cinema.CinemaReservationSystem.user.UserFixtures.USER_ID;
import static com.bokeher.cinema.CinemaReservationSystem.user.UserFixtures.anyUser;
import static org.junit.jupiter.api.Assertions.*;

import static com.bokeher.cinema.CinemaReservationSystem.reservation.ReservationFixtures.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ScreeningService screeningService;

    private ReservationService reservationService;

    @Mock
    private UserPrincipal userPrincipal;

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(
                reservationRepository,
                seatRepository,
                screeningService,
                new ReservationMapper(
                        new ScreeningMapper(new MovieMapper(), new RoomMapper(new SeatMapper())),
                        new UserMapper(),
                        new SeatMapper()
                )
        );

        userPrincipal = new UserPrincipal(anyUser().build());
    }

    @Test
    void createReservation_shouldCreateReservation() {
        Reservation reservation = anyReservation().build();
        CreateReservationRequest request = anyCreateReservationRequest().build();

        when(seatRepository.findById(request.getSeatId())).thenReturn(Optional.of(reservation.getSeat()));
        when(screeningService.getById(request.getScreeningId())).thenReturn(reservation.getScreening());
        when(reservationRepository.existsByScreeningIdAndSeatIdAndStatusIn(
                request.getScreeningId(),
                request.getSeatId(),
                ACTIVE_STATUSES
        )).thenReturn(false);

        when(screeningService.isSeatValid(request.getScreeningId(), request.getSeatId())).thenReturn(true);
        when(reservationRepository.save(any())).thenReturn(reservation);

        ReservationResponse response = reservationService.createReservation(request, userPrincipal);

        assertReservationResponse(reservation, response);

        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository).save(captor.capture());

        Reservation capturedReservation = captor.getValue();

        assertCapturedReservation(reservation, capturedReservation);
    }

    @Test
    void createReservation_shouldThrowException_whenSeatDoesNotExist() {
        CreateReservationRequest request = anyCreateReservationRequest().build();

        when(seatRepository.findById(request.getSeatId())).thenReturn(Optional.empty());

        assertThrows(
                SeatNotFoundException.class,
                () -> reservationService.createReservation(request, userPrincipal)
        );
    }

    @Test
    void createReservation_shouldThrowException_whenScreeningDoesNotExist() {
        Reservation reservation = anyReservation().build();
        CreateReservationRequest request = anyCreateReservationRequest().build();

        when(seatRepository.findById(request.getSeatId())).thenReturn(Optional.of(reservation.getSeat()));
        when(screeningService.getById(request.getScreeningId()))
                .thenThrow(new ScreeningNotFoundException(request.getScreeningId()));

        assertThrows(
                ScreeningNotFoundException.class,
                () -> reservationService.createReservation(request, userPrincipal)
        );
    }

    @Test
    void createReservation_shouldThrowException_whenSeatTaken() {
        Reservation reservation = anyReservation().build();
        CreateReservationRequest request = anyCreateReservationRequest().build();

        when(seatRepository.findById(request.getSeatId())).thenReturn(Optional.of(reservation.getSeat()));
        when(screeningService.getById(request.getScreeningId())).thenReturn(reservation.getScreening());
        when(reservationRepository.existsByScreeningIdAndSeatIdAndStatusIn(
                request.getScreeningId(),
                request.getSeatId(),
                ACTIVE_STATUSES
        )).thenReturn(true);

        assertThrows(
                SeatAlreadyTakenException.class,
                () -> reservationService.createReservation(request, userPrincipal)
        );
    }

    @Test
    void createReservation_shouldThrowException_whenSeatDoesNotBelongToTheScreening() {
        Reservation reservation = anyReservation().build();
        CreateReservationRequest request = anyCreateReservationRequest().build();

        when(seatRepository.findById(request.getSeatId())).thenReturn(Optional.of(reservation.getSeat()));
        when(screeningService.getById(request.getScreeningId())).thenReturn(reservation.getScreening());
        when(reservationRepository.existsByScreeningIdAndSeatIdAndStatusIn(
                request.getScreeningId(),
                request.getSeatId(),
                ACTIVE_STATUSES
        )).thenReturn(false);
        when(screeningService.isSeatValid(request.getScreeningId(), request.getSeatId())).thenReturn(false);

        assertThrows(
                SeatDoesNotBelongToScreeningException.class,
                () -> reservationService.createReservation(request, userPrincipal)
        );
    }

    @Test
    void createReservation_shouldThrowSeatAlreadyTakenException_whenDBThrowsDataIntegrityViolationException() {
        Reservation reservation = anyReservation().build();
        CreateReservationRequest request = anyCreateReservationRequest().build();

        when(seatRepository.findById(request.getSeatId())).thenReturn(Optional.of(reservation.getSeat()));
        when(screeningService.getById(request.getScreeningId())).thenReturn(reservation.getScreening());
        when(reservationRepository.existsByScreeningIdAndSeatIdAndStatusIn(
                request.getScreeningId(),
                request.getSeatId(),
                ACTIVE_STATUSES
        )).thenReturn(false);

        when(screeningService.isSeatValid(request.getScreeningId(), request.getSeatId())).thenReturn(true);
        when(reservationRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        assertThrows(
                SeatAlreadyTakenException.class,
                () -> reservationService.createReservation(request, userPrincipal)
        );
    }

    @Test
    void findById_shouldReturnReservation() {
        Reservation reservation = anyReservation()
                .user(anyUser().id(USER_ID).build())
                .build();

        userPrincipal = new UserPrincipal(anyUser().id(USER_ID).build());

        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));

        ReservationResponse response = reservationService.findById(RESERVATION_ID, userPrincipal);

        assertReservationResponse(reservation, response);
    }

    @Test
    void findById_shouldThrowException_whenReservationDoesNotBelongToUser() {
        Reservation reservation = anyReservation()
                .user(anyUser().id(40L).build()                )
                .build();

        userPrincipal = new UserPrincipal(anyUser().id(USER_ID).build());

        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));

        assertThrows(
                ReservationAccessDeniedException.class,
                () -> reservationService.findById(RESERVATION_ID, userPrincipal)
        );
    }

    @Test
    void findById_shouldThrowException_whenReservationDoesNotExist() {
        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.empty());

        assertThrows(
                ReservationNotFoundException.class,
                () -> reservationService.findById(RESERVATION_ID, userPrincipal)
        );
    }

    @Test
    void findAllForCurrentUser_shouldReturnAllUserReservations() {
        List<Reservation> reservations = List.of(
                anyReservation()
                        .id(1L)
                        .build(),
                anyReservation()
                        .id(2L)
                        .build(),
                anyReservation()
                        .id(3L)
                        .build()
        );

        when(reservationRepository.findAllByUserId(USER_ID)).thenReturn(reservations);


        userPrincipal = new UserPrincipal(anyUser().id(USER_ID).build());

        List<ReservationResponse> responses = reservationService.findAllForCurrentUser(userPrincipal);

        assertEquals(3, responses.size());
        assertReservationResponse(reservations.get(0), responses.get(0));
        assertReservationResponse(reservations.get(1), responses.get(1));
        assertReservationResponse(reservations.get(2), responses.get(2));
    }

    @Test
    void cancelReservation_shouldCancelReservation() {
        Reservation reservation = anyReservation().build();

        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));

        reservationService.cancelReservation(RESERVATION_ID, userPrincipal);

        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
        assertNull(reservation.getActive());

        verify(reservationRepository).save(reservation);
    }

    @Test
    void cancelReservation_shouldThrowException_whenReservationDoesNotExist() {
        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.empty());

        assertThrows(
                ReservationNotFoundException.class,
                () -> reservationService.cancelReservation(RESERVATION_ID, userPrincipal)
        );
    }

    @Test
    void cancelReservation_shouldThrowException_whenReservationDoesNotBelongToUser() {
        Reservation reservation = anyReservation()
                .user(anyUser().id(40L).build())
                .build();

        userPrincipal = new UserPrincipal(
                anyUser().id(USER_ID).build()
        );

        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));

        assertThrows(
                ReservationAccessDeniedException.class,
                () -> reservationService.cancelReservation(RESERVATION_ID, userPrincipal)
        );
    }

    @Test
    void cancelReservation_shouldThrowException_whenReservationIsAlreadyCancelled() {
        Reservation reservation = anyReservation()
                .status(ReservationStatus.CANCELLED)
                .active(null)
                .build();

        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));

        assertThrows(
                ReservationAlreadyCancelledException.class,
                () -> reservationService.cancelReservation(RESERVATION_ID, userPrincipal)
        );
    }

    @Test
    void confirmReservation_shouldConfirmReservation() {
        Reservation reservation = anyReservation().build();

        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));

        reservationService.confirmReservation(RESERVATION_ID, userPrincipal);

        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
        assertTrue(reservation.getActive());

        verify(reservationRepository).save(reservation);
    }

    @Test
    void confirmReservation_shouldThrowException_whenReservationDoesNotExist() {
        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.empty());

        assertThrows(
                ReservationNotFoundException.class,
                () -> reservationService.confirmReservation(RESERVATION_ID, userPrincipal)
        );
    }

    @Test
    void confirmReservation_shouldThrowException_whenReservationDoesNotBelongToUser() {
        Reservation reservation = anyReservation()
                .user(anyUser().id(40L).build())
                .build();

        userPrincipal = new UserPrincipal(
                anyUser().id(USER_ID).build()
        );

        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));

        assertThrows(
                ReservationAccessDeniedException.class,
                () -> reservationService.confirmReservation(RESERVATION_ID, userPrincipal)
        );
    }

    @Test
    void confirmReservation_shouldThrowException_whenReservationIsAlreadyConfirmed() {
        Reservation reservation = anyReservation()
                .status(ReservationStatus.CONFIRMED)
                .active(true)
                .build();

        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));

        assertThrows(
                ReservationAlreadyConfirmedException.class,
                () -> reservationService.confirmReservation(RESERVATION_ID, userPrincipal)
        );
    }

    @Test
    void confirmReservation_shouldThrowException_whenTryingToConfirmCancelledReservation() {
        Reservation reservation = anyReservation()
                .status(ReservationStatus.CANCELLED)
                .active(null)
                .build();

        when(reservationRepository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));

        assertThrows(
                ReservationCannotBeConfirmedWhenCancelledException.class,
                () -> reservationService.confirmReservation(RESERVATION_ID, userPrincipal)
        );
    }

}