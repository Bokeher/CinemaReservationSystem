package com.bokeher.cinema.CinemaReservationSystem.auth.dto;

import com.bokeher.cinema.CinemaReservationSystem.validation.annotation.user.ValidPassword;
import com.bokeher.cinema.CinemaReservationSystem.validation.annotation.user.ValidUsername;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserRequest {

    @NotBlank(message = "Username is required")
    @ValidUsername
    private String username;

    @NotBlank(message = "Password is required")
    @ValidPassword
    private String password;
}
