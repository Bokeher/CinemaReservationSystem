package com.bokeher.cinema.CinemaReservationSystem.screening.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BriefScreeningResponse {

    private Long id;

    private Long movieId;
    private String movieTitle;
    private Duration movieDuration;

    private Long roomId;
    private String roomName;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
