package com.bokeher.cinema.CinemaReservationSystem.movie.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMovieRequest {

    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    @PositiveOrZero(message = "Required age must be zero or greater")
    private Integer requiredAge;

    @Positive(message = "Duration must be greater than 0")
    private Integer durationMinutes;
}