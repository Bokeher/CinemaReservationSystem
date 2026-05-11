package com.bokeher.cinema.CinemaReservationSystem.user.exception;

import com.bokeher.cinema.CinemaReservationSystem.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ApiException {
    public InvalidCredentialsException() {
        super("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }
}
