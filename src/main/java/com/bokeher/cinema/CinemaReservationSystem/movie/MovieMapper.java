package com.bokeher.cinema.CinemaReservationSystem.movie;

import com.bokeher.cinema.CinemaReservationSystem.movie.dto.CreateMovieRequest;
import com.bokeher.cinema.CinemaReservationSystem.movie.dto.MovieResponse;
import com.bokeher.cinema.CinemaReservationSystem.movie.dto.UpdateMovieRequest;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class MovieMapper {
    public MovieResponse toResponse(Movie movie) {
        return new MovieResponse(
            movie.getId(),
            movie.getTitle(),
            movie.getDescription(),
            movie.getRequiredAge(),
            (int) movie.getDuration().toMinutes()
        );
    }

    public Movie toEntity(CreateMovieRequest request) {
        Movie movie = new Movie();

        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setRequiredAge(request.getRequiredAge());
        movie.setDuration(Duration.ofMinutes(request.getDurationMinutes()));

        return movie;
    }

    public void update(Movie movie, UpdateMovieRequest request) {
        if (request.getTitle() != null) {
            movie.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            movie.setDescription(request.getDescription());
        }

        if (request.getRequiredAge() != null) {
            movie.setRequiredAge(request.getRequiredAge());
        }

        if (request.getDurationMinutes() != null) {
            movie.setDuration(Duration.ofMinutes(request.getDurationMinutes()));
        }
    }
}
