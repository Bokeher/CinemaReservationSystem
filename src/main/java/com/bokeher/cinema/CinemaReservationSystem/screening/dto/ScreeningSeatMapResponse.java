package com.bokeher.cinema.CinemaReservationSystem.screening.dto;

import com.bokeher.cinema.CinemaReservationSystem.seat.dto.ScreeningSeatResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScreeningSeatMapResponse {

    private Long screeningId;

    private Long roomId;
    private String roomName;

    private Long movieId;
    private String movieTitle;

    private List<ScreeningSeatResponse> seats;

}
