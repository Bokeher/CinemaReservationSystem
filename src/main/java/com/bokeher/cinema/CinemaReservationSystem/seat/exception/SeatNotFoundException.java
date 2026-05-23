package com.bokeher.cinema.CinemaReservationSystem.seat.exception;

import com.bokeher.cinema.CinemaReservationSystem.ApiException;
import org.springframework.http.HttpStatus;

public class SeatNotFoundException extends ApiException {
    public SeatNotFoundException(Long id) {
        super("Seat not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
