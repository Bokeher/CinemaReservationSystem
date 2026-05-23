package com.bokeher.cinema.CinemaReservationSystem.movie;

import com.bokeher.cinema.CinemaReservationSystem.movie.dto.CreateMovieRequest;
import com.bokeher.cinema.CinemaReservationSystem.movie.dto.UpdateMovieRequest;

import java.time.Duration;

public class MovieFixtures {

    public static final Long MOVIE_ID = 1L;
    public static final String TITLE = "Inception";
    public static final String DESCRIPTION = "A mind-bending sci-fi thriller";
    public static final int REQUIRED_AGE = 13;
    public static final int DURATION_MINUTES = 148;

    public static final Long UPDATED_MOVIE_ID = 2L;
    public static final String UPDATED_TITLE = "Interstellar";
    public static final String UPDATED_DESCRIPTION = "A sci-fi story about space and time";
    public static final int UPDATED_REQUIRED_AGE = 16;
    public static final int UPDATED_DURATION_MINUTES = 169;

    public static Movie.MovieBuilder anyMovie() {
        return Movie.builder()
                .id(MOVIE_ID)
                .title(TITLE)
                .description(DESCRIPTION)
                .requiredAge(REQUIRED_AGE)
                .duration(Duration.ofMinutes(DURATION_MINUTES));
    }

    public static Movie.MovieBuilder updatedMovie() {
        return Movie.builder()
                .id(UPDATED_MOVIE_ID)
                .title(UPDATED_TITLE)
                .description(UPDATED_DESCRIPTION)
                .requiredAge(UPDATED_REQUIRED_AGE)
                .duration(Duration.ofMinutes(UPDATED_DURATION_MINUTES));
    }

    public static CreateMovieRequest.CreateMovieRequestBuilder anyCreateMovieRequest() {
        return CreateMovieRequest.builder()
                .title(TITLE)
                .description(DESCRIPTION)
                .requiredAge(REQUIRED_AGE)
                .durationMinutes(DURATION_MINUTES);
    }

    public static UpdateMovieRequest.UpdateMovieRequestBuilder anyUpdateMovieRequest() {
        return UpdateMovieRequest.builder()
                .title(UPDATED_TITLE)
                .description(UPDATED_DESCRIPTION)
                .requiredAge(UPDATED_REQUIRED_AGE)
                .durationMinutes(UPDATED_DURATION_MINUTES);
    }
}