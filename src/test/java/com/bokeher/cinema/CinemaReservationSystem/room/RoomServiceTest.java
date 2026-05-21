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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    void getById_shouldThrowException_whenRoomDoesNotExist() {
        when(roomRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> roomService.getById(ID));
    }

    @Test
    void getById_shouldReturnRoom_whenRoomExists() {
        Room room = createExampleRoom();
        when(roomRepository.findById(ID)).thenReturn(Optional.of(room));

        Room result = roomService.getById(ID);

        assertRoom(room, result);
    }

    @Test
    void findById_shouldReturnRoomResponse() {
        Room room = createExampleRoom();
        when(roomRepository.findById(ID)).thenReturn(Optional.of(room));

        RoomResponse response = roomService.findById(ID);

        assertResponseEqualsRoom(room, response);
    }

    @Test
    void createRoom_shouldCreateRoom() {
        Room room = createExampleRoom();
        CreateRoomRequest createRoomRequest = CreateRoomRequest.builder()
                .name(NAME)
                .seatsPerRow(SEATS_PER_ROW)
                .build();


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
        Room room = createExampleRoom();
        when(roomRepository.findById(ID)).thenReturn(Optional.of(room));

        roomService.deleteRoom(ID);

        verify(roomRepository).delete(room);
    }

    @Test
    void findAll_shouldReturnListOfResponses_whenRoomsExist() {
        Room room = createExampleRoom();
        Room room2 = createRoom(2L, "Room 2", List.of(1, 2));

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

    private Room createExampleRoom() {
        return createRoom(ID, NAME, SEATS_PER_ROW);
    }

    private Room createRoom(long id, String name, List<Integer> seatsPerRow) {
        Room room = Room.builder()
                .id(id)
                .name(name)
                .seats(new ArrayList<>())
                .build();

        roomSeatGenerator.generate(room, seatsPerRow);

        return room;
    }

}
