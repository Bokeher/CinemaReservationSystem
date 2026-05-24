package com.bokeher.cinema.CinemaReservationSystem.reservation.exception;

import com.bokeher.cinema.CinemaReservationSystem.ApiException;
import org.springframework.http.HttpStatus;

public class SeatDoesNotBelongToScreeningException extends ApiException {
    public SeatDoesNotBelongToScreeningException(Long seatId, Long screeningId) {
        super("Seat with id " + seatId + " does not belong to screening with id " + screeningId, HttpStatus.BAD_REQUEST);
    }
}
