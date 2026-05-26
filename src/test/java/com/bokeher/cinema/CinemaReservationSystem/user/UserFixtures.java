package com.bokeher.cinema.CinemaReservationSystem.user;

import com.bokeher.cinema.CinemaReservationSystem.user.dto.CreateUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.user.dto.UpdateUserRequest;

public class UserFixtures {

    public static final Long USER_ID = 1L;
    public static final String USERNAME = "john";
    public static final String EMAIL = "john@example.com";
    public static final String PASSWORD = "password";
    public static final String ENCODED_PASSWORD = "encoded";
    public static final UserRole ROLE = UserRole.USER;

    public static User.UserBuilder userWithId() {
        return User.builder()
                .id(USER_ID)
                .username(USERNAME)
                .email(EMAIL)
                .password(ENCODED_PASSWORD)
                .role(ROLE);
    }

    public static User.UserBuilder userWithoutId() {
        return User.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(ENCODED_PASSWORD)
                .role(ROLE);
    }

    public static CreateUserRequest.CreateUserRequestBuilder createUserRequest() {
        return CreateUserRequest.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .role(ROLE);
    }

    public static UpdateUserRequest.UpdateUserRequestBuilder updateUserRequest() {
        return UpdateUserRequest.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .role(ROLE);
    }

}
