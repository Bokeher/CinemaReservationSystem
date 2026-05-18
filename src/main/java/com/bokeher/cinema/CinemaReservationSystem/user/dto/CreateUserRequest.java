package com.bokeher.cinema.CinemaReservationSystem.user.dto;

import com.bokeher.cinema.CinemaReservationSystem.user.UserRole;
import com.bokeher.cinema.CinemaReservationSystem.validation.annotation.user.ValidEmail;
import com.bokeher.cinema.CinemaReservationSystem.validation.annotation.user.ValidPassword;
import com.bokeher.cinema.CinemaReservationSystem.validation.annotation.user.ValidUsername;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "Username is required")
    @ValidUsername
    private String username;

    @NotBlank(message = "Email is required")
    @ValidEmail
    private String email;

    @NotBlank(message = "Password is required")
    @ValidPassword
    private String password;

    @NotNull(message = "Role is required")
    private UserRole role;
}
