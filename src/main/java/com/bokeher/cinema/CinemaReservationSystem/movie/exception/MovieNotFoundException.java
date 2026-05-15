package com.bokeher.cinema.CinemaReservationSystem.movie.exception;

import com.bokeher.cinema.CinemaReservationSystem.ApiException;
import org.springframework.http.HttpStatus;

public class MovieNotFoundException extends ApiException {
    public MovieNotFoundException(Long id) {
        super("Movie not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
