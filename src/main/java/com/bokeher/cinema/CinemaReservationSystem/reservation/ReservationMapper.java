package com.bokeher.cinema.CinemaReservationSystem.reservation;

import com.bokeher.cinema.CinemaReservationSystem.reservation.dto.ReservationResponse;
import com.bokeher.cinema.CinemaReservationSystem.screening.ScreeningMapper;
import com.bokeher.cinema.CinemaReservationSystem.seat.SeatMapper;
import com.bokeher.cinema.CinemaReservationSystem.user.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    private final ScreeningMapper screeningMapper;
    private final UserMapper userMapper;
    private final SeatMapper seatMapper;

    public ReservationMapper(ScreeningMapper screeningMapper, UserMapper userMapper, SeatMapper seatMapper) {
        this.screeningMapper = screeningMapper;
        this.userMapper = userMapper;
        this.seatMapper = seatMapper;
    }

    public ReservationResponse toResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .screening(screeningMapper.toBriefResponse(reservation.getScreening()))
                .user(userMapper.toResponse(reservation.getUser()))
                .seat(seatMapper.toResponse(reservation.getSeat()))
                .status(reservation.getStatus())
                .build();
    }

}
