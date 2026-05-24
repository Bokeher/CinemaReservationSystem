package com.bokeher.cinema.CinemaReservationSystem.reservation.exception;

import com.bokeher.cinema.CinemaReservationSystem.ApiException;
import org.springframework.http.HttpStatus;

public class SeatAlreadyTakenException extends ApiException {
    public SeatAlreadyTakenException(Long id) {
        super("Seat with id " + id + " is already taken", HttpStatus.CONFLICT);
    }
}
