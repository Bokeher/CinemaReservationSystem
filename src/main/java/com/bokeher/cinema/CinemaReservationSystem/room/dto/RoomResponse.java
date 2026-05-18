package com.bokeher.cinema.CinemaReservationSystem.room.dto;

import com.bokeher.cinema.CinemaReservationSystem.seat.dto.SeatResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RoomResponse {

    private Long id;
    private String name;
    private List<SeatResponse> seats;
}
