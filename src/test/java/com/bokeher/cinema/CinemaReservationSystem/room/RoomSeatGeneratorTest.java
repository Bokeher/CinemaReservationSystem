package com.bokeher.cinema.CinemaReservationSystem.room;

import com.bokeher.cinema.CinemaReservationSystem.seat.Seat;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoomSeatGeneratorTest {

    private final RoomSeatGenerator generator = new RoomSeatGenerator();

    @Test
    void shouldGenerateSeatsCorrectly() {
        Room room = new Room();

        List<Integer> seatsPerRow = List.of(3, 1, 3);

        generator.generate(room, seatsPerRow);

        List<Seat> seats = room.getSeats();
        assertEquals(7, seats.size());
    }

    @Test
    void shouldAssignSeatNumbersCorrectly() {
        Room room = new Room();

        List<Integer> seatsPerRow = List.of(2, 3);

        generator.generate(room, seatsPerRow);

        List<Seat> seats = room.getSeats();

        List<Integer> expectedSeatNumbers = List.of(1, 2, 1, 2, 3);

        assertEquals(
                expectedSeatNumbers,
                seats.stream().map(Seat::getNumber).toList()
        );
    }

    @Test
    void shouldAssignSeatRowsCorrectly() {
        Room room = new Room();

        List<Integer> seatsPerRow = List.of(1, 2, 2, 3, 2);

        generator.generate(room, seatsPerRow);

        List<Seat> seats = room.getSeats();
        List<Integer> expectedRows = List.of(1, 2, 2, 3, 3, 4, 4, 4, 5, 5);

        assertEquals(
                expectedRows,
                seats.stream().map(Seat::getRow).toList()
        );
    }
}