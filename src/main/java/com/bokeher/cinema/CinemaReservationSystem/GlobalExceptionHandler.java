package com.bokeher.cinema.CinemaReservationSystem;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<String> handleException(ApiException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(ex.getMessage());
    }

}
