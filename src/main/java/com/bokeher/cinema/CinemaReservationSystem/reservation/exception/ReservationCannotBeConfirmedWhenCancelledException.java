package com.bokeher.cinema.CinemaReservationSystem.reservation.exception;

import com.bokeher.cinema.CinemaReservationSystem.ApiException;
import org.springframework.http.HttpStatus;

public class ReservationCannotBeConfirmedWhenCancelledException extends ApiException {
    public ReservationCannotBeConfirmedWhenCancelledException() {
        super("Cannot confirm cancelled reservation", HttpStatus.CONFLICT);
    }
}
