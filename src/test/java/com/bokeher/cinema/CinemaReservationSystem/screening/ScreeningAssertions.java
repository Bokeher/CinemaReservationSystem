package com.bokeher.cinema.CinemaReservationSystem.screening;

import com.bokeher.cinema.CinemaReservationSystem.screening.dto.BriefScreeningResponse;
import com.bokeher.cinema.CinemaReservationSystem.screening.dto.DetailedScreeningResponse;

import static com.bokeher.cinema.CinemaReservationSystem.movie.MovieAssertions.assertMovie;
import static com.bokeher.cinema.CinemaReservationSystem.movie.MovieAssertions.assertMovieResponse;
import static com.bokeher.cinema.CinemaReservationSystem.room.RoomAssertions.assertRoom;
import static com.bokeher.cinema.CinemaReservationSystem.room.RoomAssertions.assertRoomResponse;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScreeningAssertions {

    private ScreeningAssertions() {}

    public static void assertScreening(Screening expected, Screening actual) {
        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertMovie(expected.getMovie(), actual.getMovie()),
                () -> assertRoom(expected.getRoom(), actual.getRoom()),
                () -> assertEquals(expected.getStartTime(), actual.getStartTime()),
                () -> assertEquals(expected.getEndTime(), actual.getEndTime())
        );
    }

    public static void assertDetailedScreeningResponse(Screening expected, DetailedScreeningResponse actual) {
        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertMovieResponse(expected.getMovie(), actual.getMovie()),
                () -> assertRoomResponse(expected.getRoom(), actual.getRoom()),
                () -> assertEquals(expected.getStartTime(), actual.getStartTime()),
                () -> assertEquals(expected.getEndTime(), actual.getEndTime())
        );
    }

    public static void assertBriefScreeningResponse(Screening expected, BriefScreeningResponse actual) {
        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getMovie().getId(), actual.getMovieId()),
                () -> assertEquals(expected.getRoom().getId(), actual.getRoomId()),
                () -> assertEquals(expected.getStartTime(), actual.getStartTime()),
                () -> assertEquals(expected.getEndTime(), actual.getEndTime())
        );
    }
}
