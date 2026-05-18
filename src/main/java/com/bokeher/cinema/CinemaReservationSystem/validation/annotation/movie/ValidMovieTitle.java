package com.bokeher.cinema.CinemaReservationSystem.validation.annotation.movie;

import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Documented
@Size(max = 255, message = "Title must be at most 255 characters")
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMovieTitle {
}
