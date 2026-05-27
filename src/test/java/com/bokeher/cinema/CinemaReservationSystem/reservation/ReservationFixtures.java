package com.bokeher.cinema.CinemaReservationSystem.reservation;

import com.bokeher.cinema.CinemaReservationSystem.reservation.dto.CreateReservationRequest;
import com.bokeher.cinema.CinemaReservationSystem.room.Room;
import com.bokeher.cinema.CinemaReservationSystem.screening.Screening;
import com.bokeher.cinema.CinemaReservationSystem.seat.Seat;

import java.util.List;

import static com.bokeher.cinema.CinemaReservationSystem.screening.ScreeningFixtures.screeningWithId;
import static com.bokeher.cinema.CinemaReservationSystem.screening.ScreeningFixtures.screeningWithoutId;
import static com.bokeher.cinema.CinemaReservationSystem.user.UserFixtures.userWithId;

public class ReservationFixtures {

    public static final Long RESERVATION_ID = 1L;

    public static final List<ReservationStatus> ACTIVE_STATUSES =
            List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED);

    public static Reservation.ReservationBuilder reservationWithId() {
        Screening screening = screeningWithId().build();
        Room room = screening.getRoom();
        Seat seat = room.getSeats().get(0);

        return Reservation.builder()
                .id(RESERVATION_ID)
                .user(userWithId().build())
                .screening(screening)
                .seat(seat)
                .status(ReservationStatus.PENDING)
                .active(true);
    }

    public static Reservation.ReservationBuilder reservationWithoutId() {
        Screening screening = screeningWithoutId().build();
        Room room = screening.getRoom();
        Seat seat = room.getSeats().get(0);

        return Reservation.builder()
                .user(userWithId().build())
                .screening(screening)
                .seat(seat)
                .status(ReservationStatus.PENDING)
                .active(true);
    }

    public static CreateReservationRequest.CreateReservationRequestBuilder createReservationRequest() {
        Screening screening = screeningWithId().build();
        Room room = screening.getRoom();
        Seat seat = room.getSeats().get(0);

        return CreateReservationRequest.builder()
                .screeningId(screening.getId())
                .seatId(seat.getId());
    }

}
