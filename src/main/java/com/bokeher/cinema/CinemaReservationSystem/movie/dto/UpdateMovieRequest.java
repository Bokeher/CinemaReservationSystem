package com.bokeher.cinema.CinemaReservationSystem.movie.dto;

import com.bokeher.cinema.CinemaReservationSystem.validation.MovieConstants;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMovieRequest {

    @Size(
            min = MovieConstants.TITLE_MIN_SIZE,
            max = MovieConstants.TITLE_MAX_SIZE,
            message = MovieConstants.TITLE_SIZE_MESSAGE
    )
    private String title;

    @Size(
            min = MovieConstants.DESCRIPTION_MIN_SIZE,
            max = MovieConstants.DESCRIPTION_MAX_SIZE,
            message = MovieConstants.DESCRIPTION_SIZE_MESSAGE
    )
    private String description;

    @Min(
            value = MovieConstants.REQUIRED_AGE_MIN,
            message = MovieConstants.REQUIRED_AGE_MESSAGE
    )
    private Integer requiredAge;

    @Positive(
            message = MovieConstants.DURATION_MESSAGE
    )
    private Integer durationMinutes;
}