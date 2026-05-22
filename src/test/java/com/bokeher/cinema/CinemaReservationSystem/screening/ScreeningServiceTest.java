package com.bokeher.cinema.CinemaReservationSystem.screening;

import com.bokeher.cinema.CinemaReservationSystem.movie.MovieMapper;
import com.bokeher.cinema.CinemaReservationSystem.movie.MovieService;
import com.bokeher.cinema.CinemaReservationSystem.room.RoomMapper;
import com.bokeher.cinema.CinemaReservationSystem.room.RoomService;
import com.bokeher.cinema.CinemaReservationSystem.screening.dto.DetailedScreeningResponse;
import com.bokeher.cinema.CinemaReservationSystem.screening.exception.ScreeningNotFoundException;
import com.bokeher.cinema.CinemaReservationSystem.seat.SeatMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.bokeher.cinema.CinemaReservationSystem.screening.ScreeningAssertions.assertScreening;
import static com.bokeher.cinema.CinemaReservationSystem.screening.ScreeningAssertions.assertScreeningResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.bokeher.cinema.CinemaReservationSystem.screening.ScreeningFixtures.*;


@ExtendWith(MockitoExtension.class)
class ScreeningServiceTest {

    private ScreeningService screeningService;

    @Mock
    ScreeningRepository screeningRepository;

    @Mock
    MovieService movieService;

    @Mock
    RoomService roomService;

    @BeforeEach
    void setUp() {
        screeningService = new ScreeningService(
                new ScreeningMapper(new MovieMapper(), new RoomMapper(new SeatMapper())),
                screeningRepository,
                movieService,
                roomService
        );
    }

    @Test
    void getById_shouldReturnScreening_whenScreeningExists() {
        Screening screening = anyScreening().build();
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
        Screening screening = anyScreening().build();
        when(screeningRepository.findById(SCREENING_ID)).thenReturn(Optional.of(screening));

        DetailedScreeningResponse response = screeningService.findById(SCREENING_ID);

        assertScreeningResponse(screening, response);
    }

    @Test
    void deleteScreening_shouldDeleteScreening() {
        Screening screening = anyScreening().build();
        when(screeningRepository.findById(SCREENING_ID)).thenReturn(Optional.of(screening));

        screeningService.deleteScreening(SCREENING_ID);

        verify(screeningRepository).delete(screening);
    }

    @Test
    void createScreening_shouldCreateScreening() {
    }

    @Test
    void updateScreening() {
    }



    @Test
    void findAll() {
    }

}