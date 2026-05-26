package com.bokeher.cinema.CinemaReservationSystem.screening;

import com.bokeher.cinema.CinemaReservationSystem.movie.Movie;
import com.bokeher.cinema.CinemaReservationSystem.movie.MovieMapper;
import com.bokeher.cinema.CinemaReservationSystem.movie.MovieService;
import com.bokeher.cinema.CinemaReservationSystem.reservation.ReservationRepository;
import com.bokeher.cinema.CinemaReservationSystem.room.Room;
import com.bokeher.cinema.CinemaReservationSystem.room.RoomMapper;
import com.bokeher.cinema.CinemaReservationSystem.room.RoomService;
import com.bokeher.cinema.CinemaReservationSystem.screening.dto.BriefScreeningResponse;
import com.bokeher.cinema.CinemaReservationSystem.screening.dto.CreateScreeningRequest;
import com.bokeher.cinema.CinemaReservationSystem.screening.dto.DetailedScreeningResponse;
import com.bokeher.cinema.CinemaReservationSystem.screening.dto.UpdateScreeningRequest;
import com.bokeher.cinema.CinemaReservationSystem.screening.exception.RoomOccupiedException;
import com.bokeher.cinema.CinemaReservationSystem.screening.exception.ScreeningNotFoundException;
import com.bokeher.cinema.CinemaReservationSystem.seat.SeatMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.bokeher.cinema.CinemaReservationSystem.movie.MovieAssertions.assertMovie;
import static com.bokeher.cinema.CinemaReservationSystem.movie.MovieFixtures.*;
import static com.bokeher.cinema.CinemaReservationSystem.room.RoomAssertions.assertRoom;
import static com.bokeher.cinema.CinemaReservationSystem.room.RoomFixtures.*;
import static com.bokeher.cinema.CinemaReservationSystem.screening.ScreeningAssertions.assertScreening;
import static com.bokeher.cinema.CinemaReservationSystem.screening.ScreeningAssertions.assertDetailedScreeningResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static com.bokeher.cinema.CinemaReservationSystem.screening.ScreeningFixtures.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ScreeningServiceTest {

    private ScreeningService screeningService;

    @Mock
    ScreeningRepository screeningRepository;

    @Mock
    MovieService movieService;

    @Mock
    RoomService roomService;

    @Mock
    ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        screeningService = new ScreeningService(
                new ScreeningMapper(new MovieMapper(), new RoomMapper(new SeatMapper())),
                screeningRepository,
                movieService,
                roomService,
                new SeatMapper(),
                reservationRepository
        );
    }

    @Test
    void getById_shouldReturnScreening_whenScreeningExists() {
        Screening screening = screeningWithId().build();
        when(screeningRepository.findById(SCREENING_ID)).thenReturn(Optional.of(screening));

        Screening result = screeningService.getById(SCREENING_ID);

        assertScreening(screening, result);
    }

    @Test
    void getById_shouldThrowException_whenScreeningDoesNotExist() {
        when(screeningRepository.findById(SCREENING_ID)).thenReturn(Optional.empty());

        assertThrows(ScreeningNotFoundException.class, () -> screeningService.getById(SCREENING_ID));
    }

    @Test
    void findById_shouldReturnScreeningResponse() {
        Screening screening = screeningWithId().build();
        when(screeningRepository.findById(SCREENING_ID)).thenReturn(Optional.of(screening));

        DetailedScreeningResponse response = screeningService.findById(SCREENING_ID);

        assertDetailedScreeningResponse(screening, response);
    }

    @Test
    void findById_shouldThrowException_whenScreeningDoesNotExist() {
        when(screeningRepository.findById(SCREENING_ID)).thenReturn(Optional.empty());

        assertThrows(
                ScreeningNotFoundException.class,
                () -> screeningService.findById(SCREENING_ID)
        );
    }

    @Test
    void deleteScreening_shouldDeleteScreening() {
        Screening screening = screeningWithId().build();
        when(screeningRepository.findById(SCREENING_ID)).thenReturn(Optional.of(screening));

        screeningService.deleteScreening(SCREENING_ID);

        verify(screeningRepository).delete(screening);
    }

    @Test
    void createScreening_shouldCreateScreening() {
        CreateScreeningRequest request = createScreeningRequest().build();

        Movie movie = movieWithId().build();
        Room room = roomWithId().build();

        Screening screening = screeningWithId().build();

        when(movieService.getById(MOVIE_ID)).thenReturn(movie);
        when(roomService.getById(ROOM_ID)).thenReturn(room);
        when(screeningRepository.save(any(Screening.class))).thenReturn(screening);

        DetailedScreeningResponse response = screeningService.createScreening(request);

        assertDetailedScreeningResponse(screening, response);

        ArgumentCaptor<Screening> captor = ArgumentCaptor.forClass(Screening.class);
        verify(screeningRepository).save(captor.capture());

        Screening saved = captor.getValue();

        assertMovie(movie, saved.getMovie());
        assertRoom(room, saved.getRoom());

        assertAll(
                () -> assertMovie(screening.getMovie(), saved.getMovie()),
                () -> assertRoom(screening.getRoom(), saved.getRoom()),
                () -> assertEquals(screening.getStartTime(), saved.getStartTime()),
                () -> assertEquals(screening.getEndTime(), saved.getEndTime())
        );
    }

    @Test
    void createScreening_shouldThrowException_whenRoomIsOccupied() {
        CreateScreeningRequest request = createScreeningRequest().build();

        Movie movie = movieWithId().build();
        Room room = roomWithId().build();

        when(movieService.getById(MOVIE_ID)).thenReturn(movie);
        when(roomService.getById(ROOM_ID)).thenReturn(room);
        when(screeningRepository.existsOverlappingScreening(
                any(), any(), any(), any()
        )).thenReturn(true);

        assertThrows(
                RoomOccupiedException.class,
                () -> screeningService.createScreening(request)
        );
    }

    @Test
    void updateScreening_shouldUpdateScreening() {
        UpdateScreeningRequest request = updateScreeningRequest().build();

        Screening screening = screeningWithId().build();
        Screening updated = updatedScreeningWithId().build();

        when(screeningRepository.findById(SCREENING_ID)).thenReturn(Optional.of(screening));
        when(movieService.getById(UPDATED_MOVIE_ID)).thenReturn(updated.getMovie());
        when(roomService.getById(UPDATED_ROOM_ID)).thenReturn(updated.getRoom());
        when(screeningRepository.save(any(Screening.class))).thenReturn(updated);

        DetailedScreeningResponse response = screeningService.updateScreening(SCREENING_ID, request);

        assertDetailedScreeningResponse(updated, response);

        ArgumentCaptor<Screening> captor = ArgumentCaptor.forClass(Screening.class);
        verify(screeningRepository).save(captor.capture());

        Screening saved = captor.getValue();

        assertMovie(updated.getMovie(), saved.getMovie());
        assertRoom(updated.getRoom(), saved.getRoom());
    }

    @Test
    void updateScreening_shouldThrowException_whenScreeningDoesNotExist() {
        UpdateScreeningRequest request = updateScreeningRequest().build();

        when(screeningRepository.findById(SCREENING_ID)).thenReturn(Optional.empty());

        assertThrows(
                ScreeningNotFoundException.class,
                () -> screeningService.updateScreening(SCREENING_ID, request)
        );

        verify(screeningRepository, never()).save(any());
    }

    @Test
    void updateScreening_shouldThrowException_whenRoomIsOccupied() {
        UpdateScreeningRequest request = updateScreeningRequest().build();

        Screening screening = screeningWithId().build();

        when(screeningRepository.findById(SCREENING_ID)).thenReturn(Optional.of(screening));
        when(movieService.getById(UPDATED_MOVIE_ID)).thenReturn(updatedMovieWithId().build());
        when(roomService.getById(UPDATED_ROOM_ID)).thenReturn(updatedRoomWithId().build());
        when(screeningRepository.existsOverlappingScreening(
                any(), any(), any(), any()
        )).thenReturn(true);

        assertThrows(
                RoomOccupiedException.class,
                () -> screeningService.updateScreening(SCREENING_ID, request)
        );
    }

    @Test
    void findAll_shouldReturnListOfScreeningResponses() {
        Screening screening = screeningWithId().build();
        Screening screening2 = updatedScreeningWithId().build();

        when(screeningRepository.findAll()).thenReturn(List.of(screening, screening2));

        List<BriefScreeningResponse> result = screeningService.findAll();

        assertEquals(2, result.size());
        ScreeningAssertions.assertBriefScreeningResponse(screening, result.get(0));
        ScreeningAssertions.assertBriefScreeningResponse(screening2, result.get(1));
    }
}
