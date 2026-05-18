package com.bokeher.cinema.CinemaReservationSystem.validation.annotation.user;

import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Documented
@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {
}
