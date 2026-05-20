package com.bokeher.cinema.CinemaReservationSystem.room;

import com.bokeher.cinema.CinemaReservationSystem.room.dto.CreateRoomRequest;
import com.bokeher.cinema.CinemaReservationSystem.room.dto.RoomResponse;
import com.bokeher.cinema.CinemaReservationSystem.seat.SeatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomMapper {

    private final SeatMapper seatMapper;

    public RoomResponse toResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .seats(
                        room.getSeats().stream()
                                .map(seatMapper::toResponse)
                                .toList()
                )
                .build();
    }

    public Room toEntity(CreateRoomRequest request) {
        return Room.builder()
                .name(request.getName())
                .build();
    }
}
