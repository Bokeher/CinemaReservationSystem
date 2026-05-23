package com.bokeher.cinema.CinemaReservationSystem.room;

import com.bokeher.cinema.CinemaReservationSystem.room.dto.CreateRoomRequest;

import java.util.ArrayList;
import java.util.List;

public class RoomFixtures {

    public static final Long ROOM_ID = 1L;
    public static final String ROOM_NAME = "Room 1";
    public static final List<Integer> SEATS_PER_ROW = List.of(3, 2, 3);

    public static final Long UPDATED_ROOM_ID = 2L;
    public static final String UPDATED_ROOM_NAME = "Room 2";
    public static final List<Integer> UPDATED_SEATS_PER_ROW = List.of(1, 2);

    private static final RoomSeatGenerator ROOM_SEAT_GENERATOR = new RoomSeatGenerator();

    public static Room.RoomBuilder anyRoom() {
        Room room = Room.builder()
                .id(ROOM_ID)
                .name(ROOM_NAME)
                .seats(new ArrayList<>())
                .build();

        ROOM_SEAT_GENERATOR.generate(room, SEATS_PER_ROW);

        return room.toBuilder();
    }

    public static Room.RoomBuilder updatedRoom() {
        Room room = Room.builder()
                .id(UPDATED_ROOM_ID)
                .name(UPDATED_ROOM_NAME)
                .seats(new ArrayList<>())
                .build();

        ROOM_SEAT_GENERATOR.generate(room, UPDATED_SEATS_PER_ROW);

        return room.toBuilder();
    }

    public static CreateRoomRequest.CreateRoomRequestBuilder anyCreateRoomRequest() {
        return CreateRoomRequest.builder()
                .name(ROOM_NAME)
                .seatsPerRow(SEATS_PER_ROW);
    }

}