package com.bokeher.cinema.CinemaReservationSystem.movie.dto;

import com.bokeher.cinema.CinemaReservationSystem.validation.annotation.movie.ValidMovieDescription;
import com.bokeher.cinema.CinemaReservationSystem.validation.annotation.movie.ValidMovieDuration;
import com.bokeher.cinema.CinemaReservationSystem.validation.annotation.movie.ValidMovieRequiredAge;
import com.bokeher.cinema.CinemaReservationSystem.validation.annotation.movie.ValidMovieTitle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @ValidMovieTitle
    private String title;

    @ValidMovieDescription
    private String description;

    @NotNull(message = "Required age is required")
    @ValidMovieRequiredAge
    private Integer requiredAge;

    @NotNull(message = "Duration is required")
    @ValidMovieDuration
    private Integer durationMinutes;
}