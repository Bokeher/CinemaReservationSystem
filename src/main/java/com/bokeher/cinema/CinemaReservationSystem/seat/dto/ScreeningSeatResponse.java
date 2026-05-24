package com.bokeher.cinema.CinemaReservationSystem.seat.dto;

import com.bokeher.cinema.CinemaReservationSystem.seat.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ScreeningSeatResponse {

    private SeatResponse seat;
    private SeatStatus status;

}
