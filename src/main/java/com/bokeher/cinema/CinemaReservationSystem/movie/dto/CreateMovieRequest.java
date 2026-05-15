package com.bokeher.cinema.CinemaReservationSystem.movie.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMovieRequest {

    String title;
    String description;
    Integer requiredAge;
    Integer durationMinutes;

}
