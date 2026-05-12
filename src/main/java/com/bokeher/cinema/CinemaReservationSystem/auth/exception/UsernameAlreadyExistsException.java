package com.bokeher.cinema.CinemaReservationSystem.auth.exception;

import com.bokeher.cinema.CinemaReservationSystem.ApiException;
import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends ApiException {
    public UsernameAlreadyExistsException(String username) {
        super("Username already exists: " + username, HttpStatus.CONFLICT);
    }
}
