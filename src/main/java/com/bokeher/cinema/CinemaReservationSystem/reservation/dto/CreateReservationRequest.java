package com.bokeher.cinema.CinemaReservationSystem.reservation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CreateReservationRequest {

    @NotNull(message = "Screening id is required")
    @Positive(message = "Screening id must be positive")
    private Long screeningId;

    @NotNull(message = "Seat id is required")
    @Positive(message = "Seat id must be positive")
    private Long seatId;

}
