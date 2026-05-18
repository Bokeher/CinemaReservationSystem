package com.bokeher.cinema.CinemaReservationSystem.validation;

public final class UserConstants {

    private UserConstants() {}

    // USERNAME
    public static final int USERNAME_MIN_SIZE = 3;
    public static final int USERNAME_MAX_SIZE = 50;

    public static final String USERNAME_SIZE_MESSAGE =
            "Username must be between "
                    + USERNAME_MIN_SIZE
                    + " and "
                    + USERNAME_MAX_SIZE
                    + " characters";

    // PASSWORD
    public static final int PASSWORD_MIN_SIZE = 8;
    public static final int PASSWORD_MAX_SIZE = 72;

    public static final String PASSWORD_SIZE_MESSAGE =
            "Password must be between "
                    + PASSWORD_MIN_SIZE
                    + " and "
                    + PASSWORD_MAX_SIZE
                    + " characters";

    // EMAIL
    public static final int EMAIL_MIN_SIZE = 1;
    public static final int EMAIL_MAX_SIZE = 255;

    public static final String EMAIL_SIZE_MESSAGE =
            "Email must be at most "
                    + EMAIL_MAX_SIZE
                    + " characters";

    public static final String EMAIL_INVALID_MESSAGE =
            "Email must be valid";
}