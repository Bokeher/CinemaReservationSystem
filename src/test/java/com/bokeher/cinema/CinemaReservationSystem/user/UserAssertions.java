package com.bokeher.cinema.CinemaReservationSystem.user;

import com.bokeher.cinema.CinemaReservationSystem.user.dto.UserResponse;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserAssertions {

    private UserAssertions() {}

    public static void assertUser(User expected, User actual) {
        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getUsername(), actual.getUsername()),
                () -> assertEquals(expected.getEmail(), actual.getEmail()),
                () -> assertEquals(expected.getPassword(), actual.getPassword()),
                () -> assertEquals(expected.getRole(), actual.getRole())
        );
    }

    public static void assertUserResponse(User expected, UserResponse actual) {
        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getUsername(), actual.getUsername()),
                () -> assertEquals(expected.getEmail(), actual.getEmail()),
                () -> assertEquals(expected.getRole(), actual.getRole())
        );
    }

    public static void assertUserCaptured(User expected, User actual) {
        assertAll(
                () -> assertEquals(expected.getUsername(), actual.getUsername()),
                () -> assertEquals(expected.getEmail(), actual.getEmail()),
                () -> assertEquals(expected.getPassword(), actual.getPassword()),
                () -> assertEquals(expected.getRole(), actual.getRole())
        );
    }
}
