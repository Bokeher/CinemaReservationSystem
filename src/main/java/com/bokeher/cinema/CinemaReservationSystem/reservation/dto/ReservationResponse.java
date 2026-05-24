package com.bokeher.cinema.CinemaReservationSystem.reservation.dto;

import com.bokeher.cinema.CinemaReservationSystem.reservation.ReservationStatus;
import com.bokeher.cinema.CinemaReservationSystem.screening.dto.BriefScreeningResponse;
import com.bokeher.cinema.CinemaReservationSystem.seat.dto.SeatResponse;
import com.bokeher.cinema.CinemaReservationSystem.user.dto.UserResponse;
import lombok.*;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class ReservationResponse {

    private Long id;

    private UserResponse user;

    private BriefScreeningResponse screening;

    private SeatResponse seat;

    private ReservationStatus status;

}
