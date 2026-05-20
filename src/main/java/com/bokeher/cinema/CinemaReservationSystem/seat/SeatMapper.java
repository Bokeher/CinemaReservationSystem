package com.bokeher.cinema.CinemaReservationSystem.seat;

import com.bokeher.cinema.CinemaReservationSystem.seat.dto.SeatResponse;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper {

    public SeatResponse toResponse(Seat seat) {
        return SeatResponse.builder()
                .id(seat.getId())
                .row(seat.getRow())
                .number(seat.getNumber())
                .build();
    }
}
