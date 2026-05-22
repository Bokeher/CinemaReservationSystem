package com.bokeher.cinema.CinemaReservationSystem.movie;

import com.bokeher.cinema.CinemaReservationSystem.movie.dto.MovieResponse;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class MovieAssertions {

    private MovieAssertions() {}

    public static void assertMovie(Movie expected, Movie actual) {
        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getTitle(), actual.getTitle()),
                () -> assertEquals(expected.getDescription(), actual.getDescription()),
                () -> assertEquals(expected.getRequiredAge(), actual.getRequiredAge()),
                () -> assertEquals(expected.getDuration(), actual.getDuration())
        );
    }

    public static void assertMovieResponse(Movie expected, MovieResponse actual) {
        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getTitle(), actual.getTitle()),
                () -> assertEquals(expected.getDescription(), actual.getDescription()),
                () -> assertEquals(expected.getRequiredAge(), actual.getRequiredAge()),
                () -> assertEquals(expected.getDuration().toMinutes(), actual.getDurationMinutes())
        );
    }

}