package com.bokeher.cinema.CinemaReservationSystem.room;

import com.bokeher.cinema.CinemaReservationSystem.room.dto.RoomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/{id}")
    public RoomResponse getRoom(@PathVariable Long id) {
        return roomService.findById(id);
    }

}
