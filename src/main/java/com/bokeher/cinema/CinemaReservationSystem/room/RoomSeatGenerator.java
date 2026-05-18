package com.bokeher.cinema.CinemaReservationSystem.room;

import com.bokeher.cinema.CinemaReservationSystem.seat.Seat;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomSeatGenerator {

    public void generate(Room room, List<Integer> seatsPerRow) {

        for (int row = 0; row < seatsPerRow.size(); row++) {
            int seatCount = seatsPerRow.get(row);

            for (int i = 1; i <= seatCount; i++) {
                Seat seat = new Seat(
                        room,
                        row+1,
                        i

                );

                room.addSeat(seat);
            }
        }
    }

}
