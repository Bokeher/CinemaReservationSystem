package com.bokeher.cinema.CinemaReservationSystem;

import com.bokeher.cinema.CinemaReservationSystem.auth.dto.AuthResponse;
import com.bokeher.cinema.CinemaReservationSystem.auth.dto.LoginUserRequest;
import com.bokeher.cinema.CinemaReservationSystem.movie.Movie;
import com.bokeher.cinema.CinemaReservationSystem.movie.MovieRepository;
import com.bokeher.cinema.CinemaReservationSystem.reservation.Reservation;
import com.bokeher.cinema.CinemaReservationSystem.reservation.ReservationRepository;
import com.bokeher.cinema.CinemaReservationSystem.reservation.ReservationStatus;
import com.bokeher.cinema.CinemaReservationSystem.reservation.dto.CreateReservationRequest;
import com.bokeher.cinema.CinemaReservationSystem.reservation.dto.ReservationResponse;
import com.bokeher.cinema.CinemaReservationSystem.room.Room;
import com.bokeher.cinema.CinemaReservationSystem.room.RoomRepository;
import com.bokeher.cinema.CinemaReservationSystem.screening.Screening;
import com.bokeher.cinema.CinemaReservationSystem.screening.ScreeningRepository;
import com.bokeher.cinema.CinemaReservationSystem.seat.Seat;
import com.bokeher.cinema.CinemaReservationSystem.user.User;
import com.bokeher.cinema.CinemaReservationSystem.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.bokeher.cinema.CinemaReservationSystem.movie.MovieFixtures.movieWithoutId;
import static com.bokeher.cinema.CinemaReservationSystem.reservation.ReservationFixtures.reservationWithoutId;
import static com.bokeher.cinema.CinemaReservationSystem.room.RoomFixtures.roomWithoutId;
import static com.bokeher.cinema.CinemaReservationSystem.screening.ScreeningFixtures.screeningWithoutId;
import static com.bokeher.cinema.CinemaReservationSystem.user.UserFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class ReservationIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void createReservation_shouldCreateReservation() {
        User user = userWithoutId()
                .password(passwordEncoder.encode(PASSWORD))
                .build();
        userRepository.save(user);

        Movie movie = movieWithoutId().build();
        movieRepository.save(movie);

        Room room = roomWithoutId().build();
        roomRepository.save(room);

        Screening screening = screeningWithoutId()
                .movie(movie)
                .room(room)
                .build();
        screeningRepository.save(screening);

        Seat seat = screening.getRoom().getSeats().get(0);

        String token = loginAndGetToken(USERNAME, PASSWORD);

        CreateReservationRequest request = CreateReservationRequest.builder()
                .seatId(seat.getId())
                .screeningId(screening.getId())
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<CreateReservationRequest> entity =
                new HttpEntity<>(request, headers);

        ResponseEntity<ReservationResponse> response = testRestTemplate.postForEntity(
                "/reservations",
                entity,
                ReservationResponse.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();

        ReservationResponse reservationResponse = response.getBody();

        assertThat(reservationResponse).isNotNull();
        assertThat(reservationResponse.getId()).isNotNull();
        assertThat(reservationResponse.getStatus()).isEqualTo(ReservationStatus.PENDING);
        assertThat(reservationResponse.getUser().getId()).isEqualTo(user.getId());
        assertThat(reservationResponse.getSeat().getId()).isEqualTo(seat.getId());
        assertThat(reservationResponse.getScreening().getId()).isEqualTo(screening.getId());

        assertThat(reservationRepository.findById(reservationResponse.getId()))
                .isPresent();
    }

    @Test
    void createReservation_shouldThrowError_whenSeatAlreadyTaken() {
        User user = userWithoutId()
                .password(passwordEncoder.encode(PASSWORD))
                .build();
        userRepository.save(user);

        User otherUser = userWithoutId()
                .username("OtherUser")
                .email("other@example.com")
                .build();
        userRepository.save(otherUser);

        Movie movie = movieWithoutId().build();
        movieRepository.save(movie);

        Room room = roomWithoutId().build();
        roomRepository.save(room);

        Screening screening = screeningWithoutId()
                .movie(movie)
                .room(room)
                .build();
        screeningRepository.save(screening);

        Seat seat = screening.getRoom().getSeats().get(0);

        Reservation reservation = reservationWithoutId()
                .seat(seat)
                .user(otherUser)
                .screening(screening)
                .status(ReservationStatus.PENDING)
                .active(true)
                .build();

        reservationRepository.save(reservation);

        String token = loginAndGetToken(USERNAME, PASSWORD);

        CreateReservationRequest request = CreateReservationRequest.builder()
                .seatId(seat.getId())
                .screeningId(screening.getId())
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<CreateReservationRequest> entity =
                new HttpEntity<>(request, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/reservations",
                entity,
                String.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();

        assertThat(reservationRepository.count())
                .isEqualTo(1);
    }

    @Test
    void createReservation_shouldThrowError_whenSeatDoesNotBelongToScreeningRoom() {
        User user = userWithoutId()
                .password(passwordEncoder.encode(PASSWORD))
                .build();
        userRepository.save(user);

        Movie movie = movieWithoutId().build();
        movieRepository.save(movie);

        Room room1 = roomWithoutId().build();
        roomRepository.save(room1);

        Room room2 = roomWithoutId().build();
        roomRepository.save(room2);

        Screening screening = screeningWithoutId()
                .movie(movie)
                .room(room1)
                .build();
        screeningRepository.save(screening);

        Seat wrongSeat = room2.getSeats().get(0);

        String token = loginAndGetToken(USERNAME, PASSWORD);

        CreateReservationRequest request = CreateReservationRequest.builder()
                .seatId(wrongSeat.getId())
                .screeningId(screening.getId())
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<CreateReservationRequest> entity =
                new HttpEntity<>(request, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/reservations",
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(reservationRepository.count()).isEqualTo(0);
    }


    private String loginAndGetToken(String username, String password) {
        LoginUserRequest request = LoginUserRequest.builder()
                .username(username)
                .password(password)
                .build();

        ResponseEntity<AuthResponse> response = testRestTemplate.postForEntity(
                "/auth/login",
                request,
                AuthResponse.class
        );

        AuthResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getToken()).isNotBlank();

        return body.getToken();
    }

}