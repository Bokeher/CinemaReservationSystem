package com.bokeher.cinema.CinemaReservationSystem.validation;

public final class MovieConstants {

    private MovieConstants() {}

    public static final int TITLE_MIN_SIZE = 1;
    public static final int TITLE_MAX_SIZE = 255;
    public static final String TITLE_SIZE_MESSAGE =
            "Title must be between " + TITLE_MIN_SIZE + " and " + TITLE_MAX_SIZE + " characters";

    public static final int DESCRIPTION_MIN_SIZE = 1;
    public static final int DESCRIPTION_MAX_SIZE = 1000;
    public static final String DESCRIPTION_SIZE_MESSAGE =
            "Description must be between " + DESCRIPTION_MIN_SIZE + " and " + DESCRIPTION_MAX_SIZE + " characters";

    public static final int REQUIRED_AGE_MIN = 0;
    public static final String REQUIRED_AGE_MESSAGE =
            "Required age must be at least " + REQUIRED_AGE_MIN;

    public static final String DURATION_MESSAGE =
            "Duration must be greater than 0";
}