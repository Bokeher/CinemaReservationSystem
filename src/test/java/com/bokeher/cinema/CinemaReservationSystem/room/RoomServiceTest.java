package com.bokeher.cinema.CinemaReservationSystem.room;

import com.bokeher.cinema.CinemaReservationSystem.room.dto.RoomResponse;
import com.bokeher.cinema.CinemaReservationSystem.room.exception.RoomNotFoundException;
import com.bokeher.cinema.CinemaReservationSystem.seat.SeatMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    private static final Long ID = 1L;
    private static final String NAME = "Room 1";
    private static final List<Integer> SEATS_PER_ROW = List.of(3, 2, 3);

    @Mock
    private RoomRepository roomRepository;

    private RoomService roomService;
    private final RoomMapper roomMapper = new RoomMapper(new SeatMapper());
    private final RoomSeatGenerator roomSeatGenerator = new RoomSeatGenerator();

    @BeforeEach
    void setUp() {
        roomService = new RoomService(
                roomRepository,
                roomMapper,
                roomSeatGenerator
        );
    }

    @Test
    void findById_shouldReturnRoomResponse_whenRoomExists() {
        Room room = createExampleRoom();

        when(roomRepository.findById(ID)).thenReturn(Optional.of(room));

        RoomResponse response = roomService.findById(ID);

        assertNotNull(response);
        assertEquals(room.getId(), response.getId());
        assertEquals(room.getName(), response.getName());
        assertEquals(room.getSeats().size(), response.getSeats().size());

        verify(roomRepository).findById(ID);
    }

    @Test
    void findById_shouldThrowException_whenRoomDoesNotExist() {
        when(roomRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> roomService.findById(ID));

        verify(roomRepository).findById(ID);
    }

    private Room createExampleRoom() {
        Room room = Room.builder()
                .id(ID)
                .name(NAME)
                .seats(new ArrayList<>())
                .build();

        roomSeatGenerator.generate(room, SEATS_PER_ROW);

        return room;
    }

}
