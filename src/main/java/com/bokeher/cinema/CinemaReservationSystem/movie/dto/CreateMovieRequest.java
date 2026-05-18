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
public class CreateMovieRequest {

    @NotBlank(message = "Title is required")
    @Size(
            max = MovieConstants.TITLE_MAX_SIZE,
            message = MovieConstants.TITLE_SIZE_MESSAGE
    )
    private String title;

    @Size(
            max = MovieConstants.DESCRIPTION_MAX_SIZE,
            message = MovieConstants.DESCRIPTION_SIZE_MESSAGE
    )
    private String description;

    @NotNull(message = "Required age is required")
    @Min(
            value = MovieConstants.REQUIRED_AGE_MIN,
            message = MovieConstants.REQUIRED_AGE_MESSAGE
    )
    private Integer requiredAge;

    @NotNull(message = "Duration is required")
    @Positive(
            message = MovieConstants.DURATION_MESSAGE
    )
    private Integer durationMinutes;
}