package com.bokeher.cinema.CinemaReservationSystem.reservation;

import com.bokeher.cinema.CinemaReservationSystem.reservation.dto.ReservationResponse;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationResponse toResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .screening(reservation.getScreening())
                .user(reservation.getUser())
                .seat(reservation.getSeat())
                .status(reservation.getStatus())
                .build();
    }
}
