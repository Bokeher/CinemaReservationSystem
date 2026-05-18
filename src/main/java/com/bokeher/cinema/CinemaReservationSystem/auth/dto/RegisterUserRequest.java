package com.bokeher.cinema.CinemaReservationSystem.auth.dto;

import com.bokeher.cinema.CinemaReservationSystem.validation.UserConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {

    @NotBlank(message = "Username is required")
    @Size(
            min = UserConstants.USERNAME_MIN_SIZE,
            max = UserConstants.USERNAME_MAX_SIZE,
            message = UserConstants.USERNAME_SIZE_MESSAGE
    )
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = UserConstants.EMAIL_INVALID_MESSAGE)
    @Size(
            min = UserConstants.EMAIL_MIN_SIZE,
            max = UserConstants.EMAIL_MAX_SIZE,
            message = UserConstants.EMAIL_SIZE_MESSAGE
    )
    private String email;

    @NotBlank(message = "Password is required")
    @Size(
            min = UserConstants.PASSWORD_MIN_SIZE,
            max = UserConstants.PASSWORD_MAX_SIZE,
            message = UserConstants.PASSWORD_SIZE_MESSAGE
    )
    private String password;

}
