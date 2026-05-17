package com.bokeher.cinema.CinemaReservationSystem.user.dto;

import com.bokeher.cinema.CinemaReservationSystem.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    private String username;
    private String password;
    private String email;
    private UserRole role;
}
