package com.bokeher.cinema.CinemaReservationSystem.validation.annotation.movie;

import jakarta.validation.constraints.PositiveOrZero;

import java.lang.annotation.*;

@Documented
@PositiveOrZero(message = "Required age must be zero or greater")
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMovieRequiredAge {
}
