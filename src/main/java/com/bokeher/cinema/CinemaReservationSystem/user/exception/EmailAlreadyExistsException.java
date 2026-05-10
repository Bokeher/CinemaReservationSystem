package com.bokeher.cinema.CinemaReservationSystem.user.exception;

import com.bokeher.cinema.CinemaReservationSystem.ApiException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends ApiException {
    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email, HttpStatus.CONFLICT);
    }
}
