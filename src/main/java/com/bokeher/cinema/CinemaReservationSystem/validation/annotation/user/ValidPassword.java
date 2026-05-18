package com.bokeher.cinema.CinemaReservationSystem.validation.annotation.user;

import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Documented
@Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
}
