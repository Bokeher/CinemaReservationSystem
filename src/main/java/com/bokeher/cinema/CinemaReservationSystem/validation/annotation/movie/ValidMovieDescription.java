package com.bokeher.cinema.CinemaReservationSystem.validation.annotation.movie;

import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Documented
@Size(max = 1000, message = "Description must be at most 1000 characters")
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMovieDescription {
}
