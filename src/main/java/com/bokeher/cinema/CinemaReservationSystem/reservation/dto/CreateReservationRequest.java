package com.bokeher.cinema.CinemaReservationSystem.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CreateReservationRequest {

    private Long screeningId;

    private Long seatId;

}
