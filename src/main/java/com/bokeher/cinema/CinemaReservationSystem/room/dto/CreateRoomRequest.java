package com.bokeher.cinema.CinemaReservationSystem.room.dto;

import com.bokeher.cinema.CinemaReservationSystem.validation.RoomConstants;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CreateRoomRequest {

    @NotBlank(message = "Room name is required")
    @Size(
            min = RoomConstants.NAME_MIN_SIZE,
            max = RoomConstants.NAME_MAX_SIZE,
            message = RoomConstants.NAME_SIZE_MESSAGE
    )
    private String name;

    @NotNull(message = "Seats per row list is required")
    @Size(min = 1, message = "Seats per row has to have at least one element")
    private List<
            @NotNull(message = "Number of seats cannot be null")
            @Positive(message = "Number of seats in row has to be positive")
                Integer> seatsPerRow;

}
