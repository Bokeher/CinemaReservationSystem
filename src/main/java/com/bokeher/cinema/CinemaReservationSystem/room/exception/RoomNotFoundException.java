package com.bokeher.cinema.CinemaReservationSystem.room.exception;

import com.bokeher.cinema.CinemaReservationSystem.ApiException;
import org.springframework.http.HttpStatus;

public class RoomNotFoundException extends ApiException {
    public RoomNotFoundException(Long id) {
        super("Room not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
