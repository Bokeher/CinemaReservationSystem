package com.bokeher.cinema.CinemaReservationSystem.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMovieRequest {

    private String title;
    private String description;
    private Integer requiredAge;
    private Integer durationMinutes;
}
