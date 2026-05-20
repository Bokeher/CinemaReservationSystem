package com.bokeher.cinema.CinemaReservationSystem.movie;

import com.bokeher.cinema.CinemaReservationSystem.movie.dto.CreateMovieRequest;
import com.bokeher.cinema.CinemaReservationSystem.movie.dto.MovieResponse;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class MovieMapper {

    public MovieResponse toResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .requiredAge(movie.getRequiredAge())
                .durationMinutes((int) movie.getDuration().toMinutes())
                .build();
    }

    public Movie toEntity(CreateMovieRequest request) {
        return Movie.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .requiredAge(request.getRequiredAge())
                .duration(Duration.ofMinutes(request.getDurationMinutes()))
                .build();
    }

}
