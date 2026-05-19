package com.bokeher.cinema.CinemaReservationSystem.room;

import com.bokeher.cinema.CinemaReservationSystem.room.dto.CreateRoomRequest;
import com.bokeher.cinema.CinemaReservationSystem.room.dto.RoomResponse;
import com.bokeher.cinema.CinemaReservationSystem.room.exception.RoomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final RoomSeatGenerator roomSeatGenerator;

    public Room getById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));
    }

    public RoomResponse findById(Long id) {
        Room room = getById(id);

        return roomMapper.toResponse(room);
    }

    public RoomResponse createRoom(CreateRoomRequest request) {
        Room room = roomMapper.toEntity(request);

        roomSeatGenerator.generate(room, request.getSeatsPerRow());

        Room savedRoom = roomRepository.save(room);

        return roomMapper.toResponse(savedRoom);
    }

    public void deleteRoom(Long id) {
        Room room = getById(id);

        roomRepository.delete(room);
    }

    public List<RoomResponse> findAll() {
        return roomRepository.findAll().stream()
                .map(roomMapper::toResponse)
                .toList();
    }
}
