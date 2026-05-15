package com.bokeher.cinema.CinemaReservationSystem.movie;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByTitleContainingIgnoreCase(String title);

}
