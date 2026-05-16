package com.bokeher.cinema.CinemaReservationSystem.movie;

import com.bokeher.cinema.CinemaReservationSystem.movie.dto.CreateMovieRequest;
import com.bokeher.cinema.CinemaReservationSystem.movie.dto.MovieResponse;
import com.bokeher.cinema.CinemaReservationSystem.movie.dto.UpdateMovieRequest;
import com.bokeher.cinema.CinemaReservationSystem.movie.exception.MovieAlreadyExistsException;
import com.bokeher.cinema.CinemaReservationSystem.movie.exception.MovieNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public List<MovieResponse> findByTitleIgnoreCase(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(movieMapper::toResponse)
                .toList();
    }

    public MovieResponse findById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));

        return movieMapper.toResponse(movie);
    }

    public List<MovieResponse> findAll() {
        return movieRepository.findAll()
                .stream()
                .map(movieMapper::toResponse)
                .toList();
    }

    public MovieResponse createMovie(CreateMovieRequest request) {
        if(movieRepository.existsByTitle(request.getTitle())) {
            throw new MovieAlreadyExistsException(request.getTitle());
        }

        Movie movie = movieMapper.toEntity(request);

        Movie savedMovie = movieRepository.save(movie);

        return movieMapper.toResponse(savedMovie);
    }

    public List<MovieResponse> find(String title) {
        if (title == null) {
            return findAll();
        }

        return findByTitleIgnoreCase(title);
    }

    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));

        movieRepository.delete(movie);
    }

    public MovieResponse updateMovie(Long id, UpdateMovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));

        if (!Objects.equals(movie.getTitle(), request.getTitle())
                && movieRepository.existsByTitle(request.getTitle())) {

            throw new MovieAlreadyExistsException(request.getTitle());
        }

        movieMapper.update(movie, request);

        Movie savedMovie = movieRepository.save(movie);

        return movieMapper.toResponse(savedMovie);
    }
}
