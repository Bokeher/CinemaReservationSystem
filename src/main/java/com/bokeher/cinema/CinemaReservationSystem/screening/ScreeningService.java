package com.bokeher.cinema.CinemaReservationSystem.screening;

import com.bokeher.cinema.CinemaReservationSystem.movie.Movie;
import com.bokeher.cinema.CinemaReservationSystem.movie.MovieService;
import com.bokeher.cinema.CinemaReservationSystem.room.Room;
import com.bokeher.cinema.CinemaReservationSystem.room.RoomService;
import com.bokeher.cinema.CinemaReservationSystem.screening.dto.CreateScreeningRequest;
import com.bokeher.cinema.CinemaReservationSystem.screening.dto.DetailedScreeningResponse;
import com.bokeher.cinema.CinemaReservationSystem.screening.dto.UpdateScreeningRequest;
import com.bokeher.cinema.CinemaReservationSystem.screening.exception.ScreeningNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningService {
    private final ScreeningMapper screeningMapper;
    private final ScreeningRepository screeningRepository;
    private final MovieService movieService;
    private final RoomService roomService;

    public Screening getById(Long id) {
        return screeningRepository.findById(id)
                .orElseThrow(() -> new ScreeningNotFoundException(id));
    }

    public DetailedScreeningResponse createScreening(CreateScreeningRequest request) {
        Movie movie = movieService.getById(request.getMovieId());
        Room room = roomService.getById(request.getRoomId());

        Screening screening = Screening.builder()
                .movie(movie)
                .room(room)
                .startTime(request.getStartTime())
                .endTime(request.getStartTime().plusMinutes(movie.getDuration().toMinutes()))
                .build();

        Screening savedScreening = screeningRepository.save(screening);

        return screeningMapper.toDetailedResponse(savedScreening);
    }

    public void deleteScreening(Long id) {
        Screening screening = getById(id);

        screeningRepository.delete(screening);
    }

    public DetailedScreeningResponse updateScreening(Long id, UpdateScreeningRequest request) {
        Screening screening = getById(id);

        applyPatch(screening, request);

        Screening savedScreening = screeningRepository.save(screening);

        return screeningMapper.toDetailedResponse(savedScreening);
    }

    public DetailedScreeningResponse findById(Long id) {
        Screening screening = getById(id);

        return screeningMapper.toDetailedResponse(screening);
    }

    public List<DetailedScreeningResponse> findAll() {
        return screeningRepository.findAll()
                .stream()
                .map(screeningMapper::toDetailedResponse)
                .toList();
    }

    private void applyPatch(Screening screening, UpdateScreeningRequest request) {
        if (request.getMovieId() != null) {
            screening.setMovie(movieService.getById(request.getMovieId()));
        }
        if (request.getRoomId() != null) {
            screening.setRoom(roomService.getById(request.getRoomId()));
        }
        if (request.getStartTime() != null) {
            screening.setStartTime(request.getStartTime());
        }
        if(request.getStartTime() != null || request.getMovieId() != null) {
            screening.setEndTime(screening.getStartTime().plusMinutes(
                    screening.getMovie().getDuration().toMinutes()
            ));
        }
    }

}
