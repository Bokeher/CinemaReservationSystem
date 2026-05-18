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
        return new RoomResponse(
                room.getId(),
                room.getName(),
                room.getSeats().stream()
                        .map(seatMapper::toResponse)
                        .toList()
        );
    }

    public Room toEntity(CreateRoomRequest request) {
        Room room = new Room();
        room.setName(request.getName());

        return room;
    }
}
