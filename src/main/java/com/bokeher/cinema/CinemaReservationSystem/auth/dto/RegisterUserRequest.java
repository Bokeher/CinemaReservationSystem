package com.bokeher.cinema.CinemaReservationSystem.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {

    private String username;
    private String password;
    private String email;
}
