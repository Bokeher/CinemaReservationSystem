package com.bokeher.cinema.CinemaReservationSystem.auth;

import com.bokeher.cinema.CinemaReservationSystem.auth.dto.LoginUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.auth.dto.RegisterUserRequest;

import static com.bokeher.cinema.CinemaReservationSystem.user.UserFixtures.*;

public class AuthFixtures {
    public static final String TOKEN = "token";

    public static RegisterUserRequest.RegisterUserRequestBuilder registerRequest() {
        return RegisterUserRequest.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD);
    }

    public static LoginUserRequest.LoginUserRequestBuilder loginRequest() {
        return LoginUserRequest.builder()
                .username(USERNAME)
                .password(PASSWORD);
    }
}
