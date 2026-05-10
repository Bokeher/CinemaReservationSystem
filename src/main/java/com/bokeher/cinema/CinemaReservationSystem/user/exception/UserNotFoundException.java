package com.bokeher.cinema.CinemaReservationSystem.user.exception;

import com.bokeher.cinema.CinemaReservationSystem.ApiException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException(Long id) {
        super("User not found with id: " + id, HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String username) {
        super("User not found with name: " + username, HttpStatus.NOT_FOUND);
    }
}
