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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

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
        User user = createAndSaveUser(USERNAME);

        Screening screening = createAndSaveScreening();

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
    void createReservation_shouldThrowError_whenSeatAlreadyTakenAndStatusPending() {
        createAndSaveUser(USERNAME);

        User otherUser = createAndSaveUser("OtherUser");

        Screening screening = createAndSaveScreening();

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
    void createReservation_shouldThrowError_whenSeatAlreadyTakenAndStatusConfirmed() {
        createAndSaveUser(USERNAME);

        User otherUser = createAndSaveUser("OtherUser");

        Screening screening = createAndSaveScreening();

        Seat seat = screening.getRoom().getSeats().get(0);

        Reservation reservation = reservationWithoutId()
                .seat(seat)
                .user(otherUser)
                .screening(screening)
                .status(ReservationStatus.CONFIRMED)
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
        createAndSaveUser(USERNAME);

        Screening screening = createAndSaveScreening();

        Room otherRoom = roomWithoutId().build();
        roomRepository.save(otherRoom);

        Seat wrongSeat = otherRoom.getSeats().get(0);

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

    @Test
    void createReservation_shouldHandleRaceCondition() throws Exception {
        createAndSaveUser("user1");
        createAndSaveUser("user2");

        Screening screening = createAndSaveScreening();

        Seat seat = screening.getRoom().getSeats().get(0);

        String token1 = loginAndGetToken("user1", PASSWORD);
        String token2 = loginAndGetToken("user2", PASSWORD);

        CreateReservationRequest request = CreateReservationRequest.builder()
                .seatId(seat.getId())
                .screeningId(screening.getId())
                .build();

        HttpHeaders headers1 = new HttpHeaders();
        headers1.setBearerAuth(token1);

        HttpHeaders headers2 = new HttpHeaders();
        headers2.setBearerAuth(token2);

        HttpEntity<CreateReservationRequest> entity1 = new HttpEntity<>(request, headers1);
        HttpEntity<CreateReservationRequest> entity2 = new HttpEntity<>(request, headers2);

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(2);

        AtomicReference<ResponseEntity<String>> r1 = new AtomicReference<>();
        AtomicReference<ResponseEntity<String>> r2 = new AtomicReference<>();

        Runnable task1 = () -> {
            try {
                start.await();
                r1.set(testRestTemplate.postForEntity(
                        "/reservations",
                        entity1,
                        String.class
                ));
            } catch (Exception ignored) {
            } finally {
                done.countDown();
            }
        };

        Runnable task2 = () -> {
            try {
                start.await();
                r2.set(testRestTemplate.postForEntity(
                        "/reservations",
                        entity2,
                        String.class
                ));
            } catch (Exception ignored) {
            } finally {
                done.countDown();
            }
        };

        new Thread(task1).start();
        new Thread(task2).start();

        start.countDown();
        done.await();

        long success = Stream.of(r1.get(), r2.get())
                .filter(res -> res.getStatusCode().is2xxSuccessful())
                .count();

        assertThat(success).isEqualTo(1);
        assertThat(reservationRepository.count()).isEqualTo(1);
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

    private User createAndSaveUser(String username) {
        User user = userWithoutId()
                .username(username)
                .email(username + "@example.com")
                .password(passwordEncoder.encode(PASSWORD))
                .build();

        return userRepository.save(user);
    }

    private Screening createAndSaveScreening() {
        Movie movie = movieRepository.save(movieWithoutId().build());
        Room room = roomRepository.save(roomWithoutId().build());

        return screeningRepository.save(
                screeningWithoutId()
                        .movie(movie)
                        .room(room)
                        .build()
        );
    }

}