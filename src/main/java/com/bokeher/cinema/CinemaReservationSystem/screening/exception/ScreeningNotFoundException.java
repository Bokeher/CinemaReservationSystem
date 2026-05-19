package com.bokeher.cinema.CinemaReservationSystem.screening.exception;

import com.bokeher.cinema.CinemaReservationSystem.ApiException;
import org.springframework.http.HttpStatus;

public class ScreeningNotFoundException extends ApiException {
    public ScreeningNotFoundException(Long id) {
        super("Screening not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
