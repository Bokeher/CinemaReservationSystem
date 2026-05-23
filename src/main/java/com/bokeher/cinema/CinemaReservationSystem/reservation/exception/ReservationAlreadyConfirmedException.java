package com.bokeher.cinema.CinemaReservationSystem.reservation.exception;

import com.bokeher.cinema.CinemaReservationSystem.ApiException;
import org.springframework.http.HttpStatus;

public class ReservationAlreadyConfirmedException extends ApiException {
    public ReservationAlreadyConfirmedException() {
        super("Reservation is already confirmed", HttpStatus.CONFLICT);
    }
}
