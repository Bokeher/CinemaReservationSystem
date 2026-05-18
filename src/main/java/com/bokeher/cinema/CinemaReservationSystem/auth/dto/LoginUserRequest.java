package com.bokeher.cinema.CinemaReservationSystem.auth.dto;

import com.bokeher.cinema.CinemaReservationSystem.validation.UserConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(
            min = UserConstants.USERNAME_MIN_SIZE,
            max = UserConstants.USERNAME_MAX_SIZE,
            message = UserConstants.USERNAME_SIZE_MESSAGE
    )
    private String username;

    @NotBlank(message = "Password is required")
    @Size(
            min = UserConstants.PASSWORD_MIN_SIZE,
            max = UserConstants.PASSWORD_MAX_SIZE,
            message = UserConstants.PASSWORD_SIZE_MESSAGE
    )
    private String password;
}
