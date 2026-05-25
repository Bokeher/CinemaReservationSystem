package com.bokeher.cinema.CinemaReservationSystem.auth;

import com.bokeher.cinema.CinemaReservationSystem.auth.dto.AuthResponse;
import com.bokeher.cinema.CinemaReservationSystem.user.User;
import com.bokeher.cinema.CinemaReservationSystem.user.dto.UserResponse;

import static com.bokeher.cinema.CinemaReservationSystem.user.UserAssertions.assertUserResponse;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class AuthAssertions {

    private AuthAssertions() {}

    public static void assertAuthResponse(String expectedToken, User expected, AuthResponse actual) {
        UserResponse user = actual.getUser();
        assertAll(
                () -> assertUserResponse(expected, user),
                () -> assertEquals(expectedToken, actual.getToken())
        );
    }
}
