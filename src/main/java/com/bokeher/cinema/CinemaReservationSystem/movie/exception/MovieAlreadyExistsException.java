package com.bokeher.cinema.CinemaReservationSystem.movie.exception;

import com.bokeher.cinema.CinemaReservationSystem.ApiException;
import org.springframework.http.HttpStatus;

public class MovieAlreadyExistsException extends ApiException {
    public MovieAlreadyExistsException(String title) {
        super("Movie with title '" + title + "' already exists", HttpStatus.CONFLICT);
    }
}
