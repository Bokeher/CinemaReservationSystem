package com.bokeher.cinema.CinemaReservationSystem.reservation.exception;

import com.bokeher.cinema.CinemaReservationSystem.ApiException;
import org.springframework.http.HttpStatus;

public class ReservationAccessDeniedException extends ApiException {
    public ReservationAccessDeniedException(Long id) {
        super("No access to reservation with id: " + id, HttpStatus.FORBIDDEN);
    }
}
