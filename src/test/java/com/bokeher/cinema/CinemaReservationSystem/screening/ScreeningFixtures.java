package com.bokeher.cinema.CinemaReservationSystem.screening;

import com.bokeher.cinema.CinemaReservationSystem.movie.Movie;
import com.bokeher.cinema.CinemaReservationSystem.room.Room;
import com.bokeher.cinema.CinemaReservationSystem.screening.dto.CreateScreeningRequest;
import com.bokeher.cinema.CinemaReservationSystem.screening.dto.UpdateScreeningRequest;

import java.time.LocalDateTime;

import static com.bokeher.cinema.CinemaReservationSystem.movie.MovieFixtures.*;
import static com.bokeher.cinema.CinemaReservationSystem.room.RoomFixtures.*;

public class ScreeningFixtures {

    public static final Long SCREENING_ID = 1L;

    public static final LocalDateTime START_TIME =
            LocalDateTime.of(2024, 6, 1, 12, 0);

    public static final LocalDateTime UPDATED_START_TIME =
            LocalDateTime.of(2024, 6, 1, 15, 0);

    public static Screening.ScreeningBuilder screeningWithId() {
        Movie movie = movieWithId().build();
        Room room = roomWithId().build();

        return Screening.builder()
                .id(SCREENING_ID)
                .movie(movie)
                .room(room)
                .startTime(START_TIME)
                .endTime(START_TIME.plusMinutes(movie.getDuration().toMinutes()));
    }

    public static Screening.ScreeningBuilder screeningWithoutId() {
        Movie movie = movieWithoutId().build();
        Room room = roomWithoutId().build();

        return Screening.builder()
                .movie(movie)
                .room(room)
                .startTime(START_TIME)
                .endTime(START_TIME.plusMinutes(movie.getDuration().toMinutes()));
    }

    public static Screening.ScreeningBuilder updatedScreeningWithId() {
        Movie movie = updatedMovieWithId().build();
        Room room = updatedRoomWithId().build();

        return Screening.builder()
                .id(SCREENING_ID)
                .movie(movie)
                .room(room)
                .startTime(UPDATED_START_TIME)
                .endTime(UPDATED_START_TIME.plusMinutes(movie.getDuration().toMinutes()));
    }

    public static CreateScreeningRequest.CreateScreeningRequestBuilder createScreeningRequest() {
        return CreateScreeningRequest.builder()
                .movieId(MOVIE_ID)
                .roomId(ROOM_ID)
                .startTime(START_TIME);
    }

    public static UpdateScreeningRequest.UpdateScreeningRequestBuilder updateScreeningRequest() {
        return UpdateScreeningRequest.builder()
                .movieId(UPDATED_MOVIE_ID)
                .roomId(UPDATED_ROOM_ID)
                .startTime(UPDATED_START_TIME);
    }

}