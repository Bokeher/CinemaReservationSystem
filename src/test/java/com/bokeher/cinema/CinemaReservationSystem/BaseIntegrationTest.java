package com.bokeher.cinema.CinemaReservationSystem;

import com.bokeher.cinema.CinemaReservationSystem.movie.MovieRepository;
import com.bokeher.cinema.CinemaReservationSystem.reservation.ReservationRepository;
import com.bokeher.cinema.CinemaReservationSystem.room.RoomRepository;
import com.bokeher.cinema.CinemaReservationSystem.screening.ScreeningRepository;
import com.bokeher.cinema.CinemaReservationSystem.seat.SeatRepository;
import com.bokeher.cinema.CinemaReservationSystem.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(BaseIntegrationTest.TestcontainersConfiguration.class)
abstract class BaseIntegrationTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ScreeningRepository screeningRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDb() {
        reservationRepository.deleteAll();
        screeningRepository.deleteAll();
        seatRepository.deleteAll();
        roomRepository.deleteAll();
        movieRepository.deleteAll();
        userRepository.deleteAll();
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class TestcontainersConfiguration {

        @Bean
        @ServiceConnection
        MySQLContainer<?> mysqlContainer() {
            //noinspection resource
            return new MySQLContainer<>("mysql:8.4.6")
                    .withDatabaseName("cinema_test")
                    .withUsername("test")
                    .withPassword("test");
        }
    }
}
