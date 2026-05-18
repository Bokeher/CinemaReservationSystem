package com.bokeher.cinema.CinemaReservationSystem.seat;

import com.bokeher.cinema.CinemaReservationSystem.seat.dto.SeatResponse;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper {
    public SeatResponse toResponse(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getRow(),
                seat.getNumber()
        );
    }
}
