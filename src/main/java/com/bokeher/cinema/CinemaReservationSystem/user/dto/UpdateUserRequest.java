package com.bokeher.cinema.CinemaReservationSystem.user.dto;

import com.bokeher.cinema.CinemaReservationSystem.user.UserRole;
import com.bokeher.cinema.CinemaReservationSystem.validation.UserConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {

    @Size(
            min = UserConstants.USERNAME_MIN_SIZE,
            max = UserConstants.USERNAME_MAX_SIZE,
            message = UserConstants.USERNAME_SIZE_MESSAGE
    )
    private String username;

    @Email(message = UserConstants.EMAIL_INVALID_MESSAGE)
    @Size(
            min = UserConstants.EMAIL_MIN_SIZE,
            max = UserConstants.EMAIL_MAX_SIZE,
            message = UserConstants.EMAIL_SIZE_MESSAGE
    )
    private String email;

    @Size(
            min = UserConstants.PASSWORD_MIN_SIZE,
            max = UserConstants.PASSWORD_MAX_SIZE,
            message = UserConstants.PASSWORD_SIZE_MESSAGE
    )
    private String password;

    private UserRole role;

}
