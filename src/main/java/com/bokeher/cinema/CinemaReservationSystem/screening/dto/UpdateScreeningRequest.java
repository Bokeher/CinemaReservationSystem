package com.bokeher.cinema.CinemaReservationSystem.screening.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateScreeningRequest {

    private Long movieId;
    private Long roomId;
    private LocalDateTime startTime;
}
