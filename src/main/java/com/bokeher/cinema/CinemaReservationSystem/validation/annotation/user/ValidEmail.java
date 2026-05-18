package com.bokeher.cinema.CinemaReservationSystem.validation.annotation.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Documented
@Email(message = "Email must be valid")
@Size(max = 255, message = "Email must be at most 255 characters")
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {
}
