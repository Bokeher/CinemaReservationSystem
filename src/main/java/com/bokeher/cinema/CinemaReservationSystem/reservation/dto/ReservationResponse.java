package com.bokeher.cinema.CinemaReservationSystem.reservation.dto;

import com.bokeher.cinema.CinemaReservationSystem.reservation.ReservationStatus;
import com.bokeher.cinema.CinemaReservationSystem.screening.Screening;
import com.bokeher.cinema.CinemaReservationSystem.seat.Seat;
import com.bokeher.cinema.CinemaReservationSystem.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {

    private Long id;

    private User user;

    private Screening screening;

    private Seat seat;

    private ReservationStatus status;

}
