package com.bokeher.cinema.CinemaReservationSystem.reservation;

import com.bokeher.cinema.CinemaReservationSystem.reservation.dto.CreateReservationRequest;
import com.bokeher.cinema.CinemaReservationSystem.room.Room;
import com.bokeher.cinema.CinemaReservationSystem.screening.Screening;
import com.bokeher.cinema.CinemaReservationSystem.seat.Seat;

import java.util.List;

import static com.bokeher.cinema.CinemaReservationSystem.screening.ScreeningFixtures.anyScreening;
import static com.bokeher.cinema.CinemaReservationSystem.user.UserFixtures.anyUser;

public class ReservationFixtures {

    public static final Long RESERVATION_ID = 1L;

    public static final List<ReservationStatus> ACTIVE_STATUSES =
            List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED);

    public static Reservation.ReservationBuilder anyReservation() {
        Screening screening = anyScreening().build();
        Room room = screening.getRoom();
        Seat seat = room.getSeats().get(0);

        return Reservation.builder()
                .id(RESERVATION_ID)
                .user(anyUser().build())
                .screening(screening)
                .seat(seat)
                .status(ReservationStatus.PENDING)
                .active(true);
    }

    public static CreateReservationRequest.CreateReservationRequestBuilder anyCreateReservationRequest() {
        Screening screening = anyScreening().build();
        Room room = screening.getRoom();
        Seat seat = room.getSeats().get(0);

        return CreateReservationRequest.builder()
                .screeningId(screening.getId())
                .seatId(seat.getId());
    }

}
