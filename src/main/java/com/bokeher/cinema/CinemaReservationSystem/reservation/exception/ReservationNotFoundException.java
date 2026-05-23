package com.bokeher.cinema.CinemaReservationSystem.reservation.exception;

import com.bokeher.cinema.CinemaReservationSystem.ApiException;
import org.springframework.http.HttpStatus;

public class ReservationNotFoundException extends ApiException {
    public ReservationNotFoundException(Long id) {
        super("Reservation not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
