package com.bokeher.cinema.CinemaReservationSystem.screening;

import com.bokeher.cinema.CinemaReservationSystem.movie.MovieMapper;
import com.bokeher.cinema.CinemaReservationSystem.room.RoomMapper;
import com.bokeher.cinema.CinemaReservationSystem.screening.dto.DetailedScreeningResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScreeningMapper {

    private final MovieMapper movieMapper;
    private final RoomMapper roomMapper;

    public DetailedScreeningResponse toDetailedResponse(Screening screening) {
        return new DetailedScreeningResponse(
                screening.getId(),
                movieMapper.toResponse(screening.getMovie()),
                roomMapper.toResponse(screening.getRoom()),
                screening.getStartTime()
        );
    }
}
