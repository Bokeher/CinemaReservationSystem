package com.bokeher.cinema.CinemaReservationSystem.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MovieResponse {

    private Long id;
    private String title;
    private String description;
    private int requiredAge;
    private int durationMinutes;

}
