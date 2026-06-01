package com.bokeher.cinema.CinemaReservationSystem;

import com.bokeher.cinema.CinemaReservationSystem.movie.Movie;
import com.bokeher.cinema.CinemaReservationSystem.movie.MovieRepository;
import com.bokeher.cinema.CinemaReservationSystem.reservation.Reservation;
import com.bokeher.cinema.CinemaReservationSystem.reservation.ReservationRepository;
import com.bokeher.cinema.CinemaReservationSystem.reservation.ReservationStatus;
import com.bokeher.cinema.CinemaReservationSystem.room.Room;
import com.bokeher.cinema.CinemaReservationSystem.room.RoomRepository;
import com.bokeher.cinema.CinemaReservationSystem.room.RoomSeatGenerator;
import com.bokeher.cinema.CinemaReservationSystem.screening.Screening;
import com.bokeher.cinema.CinemaReservationSystem.screening.ScreeningRepository;
import com.bokeher.cinema.CinemaReservationSystem.user.User;
import com.bokeher.cinema.CinemaReservationSystem.user.UserRepository;
import com.bokeher.cinema.CinemaReservationSystem.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class DataInitializer implements CommandLineRunner {

    private final SeedProperties seedProperties;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoomSeatGenerator roomSeatGenerator;
    private final RoomRepository roomRepository;
    private final ScreeningRepository screeningRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public void run(String... args) {

        if (seedProperties.isAddAdmin()) {
            seedAdmin();
        }

        if (seedProperties.isDemoMode()) {
            seedDemoData();
        }
    }

    private void seedAdmin() {
        if (userRepository.existsByRole(UserRole.ADMIN)) return;

        User admin = User.builder()
                .username("admin")
                .email("admin@example.com")
                .password(passwordEncoder.encode("admin123"))
                .role(UserRole.ADMIN)
                .build();

        userRepository.save(admin);
    }

    private void seedDemoData() {
        if (movieRepository.count() > 0) return;

        // USER
        User user = User.builder()
                .username("user")
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .role(UserRole.USER)
                .build();

        userRepository.save(user);

        // MOVIES
        Movie inception = Movie.builder()
                .title("Inception")
                .description("Dream invasion thriller")
                .requiredAge(16)
                .duration(Duration.ofMinutes(140))
                .build();

        Movie interstellar = Movie.builder()
                .title("Interstellar")
                .description("Space exploration drama")
                .requiredAge(12)
                .duration(Duration.ofMinutes(169))
                .build();

        movieRepository.save(inception);
        movieRepository.save(interstellar);

        // ROOM
        Room room = Room.builder()
                .name("Room 1")
                .build();

        roomSeatGenerator.generate(room, List.of(5, 4, 5));
        roomRepository.save(room);

        // SCREENINGS
        LocalDateTime start1 = LocalDateTime.now().plusDays(7).withHour(14).withMinute(0).withSecond(0);
        LocalDateTime start2 = LocalDateTime.now().plusDays(7).withHour(18).withMinute(0).withSecond(0);

        Screening screening1 = Screening.builder()
                .movie(interstellar)
                .room(room)
                .startTime(start1)
                .endTime(start1.plus(interstellar.getDuration()))
                .build();

        Screening screening2 = Screening.builder()
                .movie(inception)
                .room(room)
                .startTime(start2)
                .endTime(start2.plus(inception.getDuration()))
                .build();

        screeningRepository.save(screening1);
        screeningRepository.save(screening2);

        // RESERVATION
        Reservation reservation = Reservation.builder()
                .user(user)
                .active(true)
                .status(ReservationStatus.PENDING)
                .seat(room.getSeats().get(2))
                .screening(screening1)
                .build();

        reservationRepository.save(reservation);

    }
}

