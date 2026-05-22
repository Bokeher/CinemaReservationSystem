package com.bokeher.cinema.CinemaReservationSystem.room;

import com.bokeher.cinema.CinemaReservationSystem.room.dto.RoomResponse;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class RoomAssertions {

    private RoomAssertions() {}

    public static void assertRoom(Room expected, Room actual) {
        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getSeats().size(), actual.getSeats().size())
        );
    }

    public static void assertRoomResponse(Room expected, RoomResponse actual) {
        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getSeats().size(), actual.getSeats().size())
        );
    }

}