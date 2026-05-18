package com.bokeher.cinema.CinemaReservationSystem.validation.annotation.movie;

import jakarta.validation.constraints.Positive;

import java.lang.annotation.*;

@Documented
@Positive(message = "Duration must be greater than 0")
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMovieDuration {
}
