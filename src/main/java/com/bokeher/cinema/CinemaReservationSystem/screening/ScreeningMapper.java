package com.bokeher.cinema.CinemaReservationSystem.screening;

import com.bokeher.cinema.CinemaReservationSystem.movie.Movie;
import com.bokeher.cinema.CinemaReservationSystem.movie.MovieMapper;
import com.bokeher.cinema.CinemaReservationSystem.room.Room;
import com.bokeher.cinema.CinemaReservationSystem.room.RoomMapper;
import com.bokeher.cinema.CinemaReservationSystem.screening.dto.BriefScreeningResponse;
import com.bokeher.cinema.CinemaReservationSystem.screening.dto.DetailedScreeningResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScreeningMapper {

    private final MovieMapper movieMapper;
    private final RoomMapper roomMapper;

    public DetailedScreeningResponse toDetailedResponse(Screening screening) {
        return DetailedScreeningResponse.builder()
                .id(screening.getId())
                .movie(movieMapper.toResponse(screening.getMovie()))
                .room(roomMapper.toResponse(screening.getRoom()))
                .startTime(screening.getStartTime())
                .endTime(screening.getEndTime())
                .build();
    }

    public BriefScreeningResponse toBriefResponse(Screening screening) {
        Movie movie = screening.getMovie();
        Room room = screening.getRoom();

        return BriefScreeningResponse.builder()
                .id(screening.getId())

                .movieId(movie.getId())
                .movieTitle(movie.getTitle())
                .movieDuration(movie.getDuration())

                .roomId(room.getId())
                .roomName(room.getName())

                .startTime(screening.getStartTime())
                .endTime(screening.getEndTime())

                .build();
    }
}
