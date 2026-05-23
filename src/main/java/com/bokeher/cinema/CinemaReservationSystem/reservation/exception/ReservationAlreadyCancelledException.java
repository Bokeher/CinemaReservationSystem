package com.bokeher.cinema.CinemaReservationSystem.reservation.exception;

import com.bokeher.cinema.CinemaReservationSystem.ApiException;
import org.springframework.http.HttpStatus;

public class ReservationAlreadyCancelledException extends ApiException {
    public ReservationAlreadyCancelledException() {
        super("Reservation is already cancelled", HttpStatus.CONFLICT);
    }
}
