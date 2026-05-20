package com.bokeher.cinema.CinemaReservationSystem.screening.exception;

import com.bokeher.cinema.CinemaReservationSystem.ApiException;
import org.springframework.http.HttpStatus;

public class RoomOccupiedException extends ApiException {
    public RoomOccupiedException() {
        super("Room is already occupied at this time", HttpStatus.CONFLICT);
    }
}
