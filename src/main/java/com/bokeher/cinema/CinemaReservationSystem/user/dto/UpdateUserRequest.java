package com.bokeher.cinema.CinemaReservationSystem.user.dto;

import com.bokeher.cinema.CinemaReservationSystem.user.UserRole;
import com.bokeher.cinema.CinemaReservationSystem.validation.annotation.user.ValidEmail;
import com.bokeher.cinema.CinemaReservationSystem.validation.annotation.user.ValidPassword;
import com.bokeher.cinema.CinemaReservationSystem.validation.annotation.user.ValidUsername;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    @ValidUsername
    private String username;

    @ValidEmail
    private String email;

    @ValidPassword
    private String password;

    private UserRole role;
}
