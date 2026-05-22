package com.bokeher.cinema.CinemaReservationSystem.room;

import com.bokeher.cinema.CinemaReservationSystem.room.dto.CreateRoomRequest;
import com.bokeher.cinema.CinemaReservationSystem.room.dto.RoomResponse;
import com.bokeher.cinema.CinemaReservationSystem.room.exception.RoomNotFoundException;
import com.bokeher.cinema.CinemaReservationSystem.seat.SeatMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.bokeher.cinema.CinemaReservationSystem.room.RoomFixtures.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

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
    void getById_shouldThrowException_whenRoomDoesNotExist() {
        when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> roomService.getById(ROOM_ID));
    }

    @Test
    void getById_shouldReturnRoom_whenRoomExists() {
        Room room = anyRoom().build();
        when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.of(room));

        Room result = roomService.getById(ROOM_ID);

        assertRoom(room, result);
    }

    @Test
    void findById_shouldReturnRoomResponse() {
        Room room = anyRoom().build();
        when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.of(room));

        RoomResponse response = roomService.findById(ROOM_ID);

        assertResponseEqualsRoom(room, response);
    }

    @Test
    void createRoom_shouldCreateRoom() {
        Room room = anyRoom().build();
        CreateRoomRequest createRoomRequest = anyCreateRequest().build();

        when(roomRepository.save(any(Room.class))).thenReturn(room);

        RoomResponse response = roomService.createRoom(createRoomRequest);

        assertResponseEqualsRoom(room, response);

        ArgumentCaptor<Room> roomCaptor = ArgumentCaptor.forClass(Room.class);
        verify(roomRepository).save(roomCaptor.capture());

        Room savedRoom = roomCaptor.getValue();

        assertEquals(room.getName(), savedRoom.getName());
        assertFalse(savedRoom.getSeats().isEmpty());
    }

    @Test
    void deleteRoom_shouldDeleteRoom() {
        Room room = anyRoom().build();
        when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.of(room));

        roomService.deleteRoom(ROOM_ID);

        verify(roomRepository).delete(room);
    }

    @Test
    void findAll_shouldReturnListOfResponses_whenRoomsExist() {
        Room room = anyRoom().build();
        Room room2 = updatedRoom().build();

        when(roomRepository.findAll()).thenReturn(List.of(room, room2));

        List<RoomResponse> response = roomService.findAll();

        assertEquals(2, response.size());
        assertResponseEqualsRoom(room, response.get(0));
        assertResponseEqualsRoom(room2, response.get(1));
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoRoomExist() {
        when(roomRepository.findAll()).thenReturn(List.of());

        List<RoomResponse> response = roomService.findAll();

        assertTrue(response.isEmpty());
    }

    private void assertResponseEqualsRoom(Room expectedRoom, RoomResponse actualResponse) {
        assertEquals(expectedRoom.getId(), actualResponse.getId());
        assertEquals(expectedRoom.getName(), actualResponse.getName());
        assertEquals(expectedRoom.getSeats().size(), actualResponse.getSeats().size());
    }

    private void assertRoom(Room expectedRoom, Room actualRoom) {
        assertEquals(expectedRoom.getId(), actualRoom.getId());
        assertEquals(expectedRoom.getName(), actualRoom.getName());
        assertEquals(expectedRoom.getSeats().size(), actualRoom.getSeats().size());
    }

}