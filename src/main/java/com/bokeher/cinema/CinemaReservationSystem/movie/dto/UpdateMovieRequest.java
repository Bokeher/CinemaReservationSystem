package com.bokeher.cinema.CinemaReservationSystem.movie.dto;

import com.bokeher.cinema.CinemaReservationSystem.validation.annotation.movie.ValidMovieDescription;
import com.bokeher.cinema.CinemaReservationSystem.validation.annotation.movie.ValidMovieDuration;
import com.bokeher.cinema.CinemaReservationSystem.validation.annotation.movie.ValidMovieRequiredAge;
import com.bokeher.cinema.CinemaReservationSystem.validation.annotation.movie.ValidMovieTitle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMovieRequest {

    @ValidMovieTitle
    private String title;

    @ValidMovieDescription
    private String description;

    @ValidMovieRequiredAge
    private Integer requiredAge;

    @ValidMovieDuration
    private Integer durationMinutes;
}