package com.bokeher.cinema.CinemaReservationSystem.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CreateRoomRequest {

    private String name;
    private List<Integer> seatsPerRow;

}
