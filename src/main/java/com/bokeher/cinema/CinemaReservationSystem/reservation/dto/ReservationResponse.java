package com.bokeher.cinema.CinemaReservationSystem.reservation.dto;

import com.bokeher.cinema.CinemaReservationSystem.reservation.ReservationStatus;
import com.bokeher.cinema.CinemaReservationSystem.screening.Screening;
import com.bokeher.cinema.CinemaReservationSystem.seat.Seat;
import com.bokeher.cinema.CinemaReservationSystem.user.User;
import lombok.*;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class ReservationResponse {

    private Long id;

    private User user;

    private Screening screening;

    private Seat seat;

    private ReservationStatus status;

}
