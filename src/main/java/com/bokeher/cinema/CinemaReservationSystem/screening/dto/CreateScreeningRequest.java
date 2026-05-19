package com.bokeher.cinema.CinemaReservationSystem.screening.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateScreeningRequest {

    @NotNull(message = "Movie id is required")
    @Positive(message = "Movie id must be positive")
    private Long movieId;

    @NotNull(message = "Room id is required")
    @Positive(message = "Room id must be positive")
    private Long roomId;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;
}
