package com.bokeher.cinema.CinemaReservationSystem.screening;

import com.bokeher.cinema.CinemaReservationSystem.movie.Movie;
import com.bokeher.cinema.CinemaReservationSystem.movie.MovieService;
import com.bokeher.cinema.CinemaReservationSystem.reservation.ReservationRepository;
import com.bokeher.cinema.CinemaReservationSystem.room.Room;
import com.bokeher.cinema.CinemaReservationSystem.room.RoomService;
import com.bokeher.cinema.CinemaReservationSystem.screening.dto.*;
import com.bokeher.cinema.CinemaReservationSystem.screening.exception.RoomOccupiedException;
import com.bokeher.cinema.CinemaReservationSystem.screening.exception.ScreeningNotFoundException;
import com.bokeher.cinema.CinemaReservationSystem.seat.Seat;
import com.bokeher.cinema.CinemaReservationSystem.seat.SeatMapper;
import com.bokeher.cinema.CinemaReservationSystem.seat.SeatStatus;
import com.bokeher.cinema.CinemaReservationSystem.seat.dto.ScreeningSeatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScreeningService {
    private final ScreeningMapper screeningMapper;
    private final ScreeningRepository screeningRepository;
    private final MovieService movieService;
    private final RoomService roomService;
    private final SeatMapper seatMapper;
    private final ReservationRepository reservationRepository;

    public Screening getById(Long id) {
        return screeningRepository.findById(id)
                .orElseThrow(() -> new ScreeningNotFoundException(id));
    }

    @Transactional
    public DetailedScreeningResponse createScreening(CreateScreeningRequest request) {
        Movie movie = movieService.getById(request.getMovieId());
        Room room = roomService.getById(request.getRoomId());

        Screening screening = Screening.builder()
                .movie(movie)
                .room(room)
                .startTime(request.getStartTime())
                .endTime(request.getStartTime().plusMinutes(movie.getDuration().toMinutes()))
                .build();

        validateScreening(screening);

        Screening savedScreening = screeningRepository.save(screening);

        return screeningMapper.toDetailedResponse(savedScreening);
    }

    @Transactional
    public void deleteScreening(Long id) {
        Screening screening = getById(id);

        screeningRepository.delete(screening);
    }

    @Transactional
    public DetailedScreeningResponse updateScreening(Long id, UpdateScreeningRequest request) {
        Screening screening = getById(id);

        applyPatch(screening, request);

        validateScreening(screening);

        Screening savedScreening = screeningRepository.save(screening);

        return screeningMapper.toDetailedResponse(savedScreening);
    }

    private void validateScreening(Screening screening) {
        boolean overlaps = screeningRepository.existsOverlappingScreening(
                screening.getRoom().getId(),
                screening.getId(),
                screening.getStartTime(),
                screening.getEndTime()
        );

        if (overlaps) {
            throw new RoomOccupiedException();
        }
    }

    public DetailedScreeningResponse findById(Long id) {
        Screening screening = getById(id);

        return screeningMapper.toDetailedResponse(screening);
    }

    public List<BriefScreeningResponse> findAll() {
        return screeningRepository.findAll()
                .stream()
                .map(screeningMapper::toBriefResponse)
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

    public boolean isSeatValid(Long screeningId, Long seatId) {
        return screeningRepository.existsByIdAndRoomSeatsId(screeningId, seatId);
    }

    public ScreeningSeatMapResponse getScreeningSeatMap(Long id) {
        Screening screening = getById(id);

        Set<Long> reservedSeatIds = reservationRepository.findReservedSeatIds(screening.getId());

        Room room = screening.getRoom();
        List<Seat> seats = room.getSeats();

        return ScreeningSeatMapResponse.builder()
                .screeningId(screening.getId())
                .roomId(room.getId())
                .roomName(room.getName())
                .movieId(screening.getMovie().getId())
                .movieTitle(screening.getMovie().getTitle())
                .seats(seats.stream()
                        .map(seat -> new ScreeningSeatResponse(
                                seatMapper.toResponse(seat),
                                reservedSeatIds.contains(seat.getId())
                                        ? SeatStatus.RESERVED
                                        : SeatStatus.AVAILABLE
                        ))
                        .toList())
                .build();

    }
}
