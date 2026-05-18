package com.bokeher.cinema.CinemaReservationSystem.auth.dto;

import com.bokeher.cinema.CinemaReservationSystem.validation.annotation.user.ValidEmail;
import com.bokeher.cinema.CinemaReservationSystem.validation.annotation.user.ValidPassword;
import com.bokeher.cinema.CinemaReservationSystem.validation.annotation.user.ValidUsername;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {

    @NotBlank(message = "Username is required")
    @ValidUsername
    private String username;

    @NotBlank(message = "Email is required")
    @ValidEmail
    private String email;

    @NotBlank(message = "Password is required")
    @ValidPassword
    private String password;
}
