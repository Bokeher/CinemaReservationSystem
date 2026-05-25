package com.bokeher.cinema.CinemaReservationSystem.validation;

public final class RoomConstants {

    private RoomConstants() {}

    public static final int NAME_MIN_SIZE = 3;
    public static final int NAME_MAX_SIZE = 50;
    public static final String NAME_SIZE_MESSAGE =
            "Room name must be between " + NAME_MIN_SIZE + " and " + NAME_MAX_SIZE + " characters";

}
