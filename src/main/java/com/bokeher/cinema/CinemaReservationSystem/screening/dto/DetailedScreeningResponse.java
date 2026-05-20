package com.bokeher.cinema.CinemaReservationSystem.screening.dto;

import com.bokeher.cinema.CinemaReservationSystem.movie.dto.MovieResponse;
import com.bokeher.cinema.CinemaReservationSystem.room.dto.RoomResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailedScreeningResponse {

    private Long id;
    private MovieResponse movie;
    private RoomResponse room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
