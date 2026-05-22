package com.bokeher.cinema.CinemaReservationSystem.movie;

import com.bokeher.cinema.CinemaReservationSystem.movie.dto.CreateMovieRequest;
import com.bokeher.cinema.CinemaReservationSystem.movie.dto.MovieResponse;
import com.bokeher.cinema.CinemaReservationSystem.movie.dto.UpdateMovieRequest;
import com.bokeher.cinema.CinemaReservationSystem.movie.exception.MovieAlreadyExistsException;
import com.bokeher.cinema.CinemaReservationSystem.movie.exception.MovieNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.bokeher.cinema.CinemaReservationSystem.movie.MovieAssertions.*;
import static com.bokeher.cinema.CinemaReservationSystem.movie.MovieFixtures.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    private MovieService movieService;

    @BeforeEach
    void setUp() {
        movieService = new MovieService(movieRepository, new MovieMapper());
    }

    @Test
    void getById_shouldReturnMovie_whenMovieExists() {
        Movie movie = anyMovie().build();
        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(movie));

        Movie result = movieService.getById(MOVIE_ID);

        assertMovie(movie, result);
    }

    @Test
    void getById_shouldThrowException_whenMovieDoesNotExist() {
        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.getById(MOVIE_ID));
    }

    @Test
    void findById_shouldReturnMovieResponse_whenMovieExists() {
        Movie movie = anyMovie().build();
        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(movie));

        MovieResponse response = movieService.findById(MOVIE_ID);

        assertMovieResponse(movie, response);
    }

    @Test
    void findAll_shouldReturnListOfResponses_whenMoviesExist() {
        Movie movie = anyMovie().build();
        Movie movie2 = updatedMovie().build();

        when(movieRepository.findAll()).thenReturn(List.of(movie, movie2));

        List<MovieResponse> movieResponses = movieService.findAll();

        assertEquals(2, movieResponses.size());
        assertMovieResponse(movie, movieResponses.get(0));
        assertMovieResponse(movie2, movieResponses.get(1));
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoMovieExist() {
        when(movieRepository.findAll()).thenReturn(List.of());

        List<MovieResponse> response = movieService.findAll();

        assertTrue(response.isEmpty());
    }

    @Test
    void find_shouldReturnAllMovies_whenTitleIsNull() {
        Movie movie = anyMovie().build();
        Movie movie2 = updatedMovie().build();

        when(movieRepository.findAll()).thenReturn(List.of(movie, movie2));

        List<MovieResponse> movieResponses = movieService.find(null);

        assertEquals(2, movieResponses.size());
        assertMovieResponse(movie, movieResponses.get(0));
        assertMovieResponse(movie2, movieResponses.get(1));
        verify(movieRepository).findAll();
    }

    @Test
    void find_shouldReturnOneMovie_whenTitleNotNull() {
        Movie movie = anyMovie().build();

        when(movieRepository.findByTitleContainingIgnoreCase(TITLE))
                .thenReturn(List.of(movie));

        List<MovieResponse> movieResponses = movieService.find(TITLE);

        assertEquals(1, movieResponses.size());
        assertMovieResponse(movie, movieResponses.get(0));

        verify(movieRepository).findByTitleContainingIgnoreCase(TITLE);
    }

    @Test
    void createMovie_shouldCreateMovie_whenTitleDoesNotExist() {
        CreateMovieRequest request = anyCreateRequest().build();
        Movie movie = anyMovie().build();

        when(movieRepository.existsByTitle(TITLE)).thenReturn(false);
        when(movieRepository.save(any())).thenReturn(movie);

        MovieResponse response = movieService.createMovie(request);

        assertMovieResponse(movie, response);

        ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);

        verify(movieRepository).existsByTitle(TITLE);
        verify(movieRepository).save(movieCaptor.capture());

        Movie savedMovie = movieCaptor.getValue();

        assertAll(
                () -> assertEquals(TITLE, savedMovie.getTitle()),
                () -> assertEquals(DESCRIPTION, savedMovie.getDescription()),
                () -> assertEquals(REQUIRED_AGE, savedMovie.getRequiredAge()),
                () -> assertEquals(DURATION_MINUTES, savedMovie.getDuration().toMinutes())
        );
    }

    @Test
    void createMovie_shouldThrowException_whenTitleExists() {
        CreateMovieRequest request = anyCreateRequest().build();

        when(movieRepository.existsByTitle(TITLE)).thenReturn(true);

        assertThrows(
                MovieAlreadyExistsException.class,
                () -> movieService.createMovie(request)
        );
    }

    @Test
    void deleteMovie_shouldDeleteMovie() {
        Movie movie = anyMovie().build();
        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(movie));

        movieService.deleteMovie(MOVIE_ID);

        verify(movieRepository).delete(movie);
    }

    @Test
    void updateMovie_shouldUpdateMovie_whenCompleteRequest() {
        UpdateMovieRequest request = anyUpdateRequest().build();
        Movie movie = anyMovie().build();
        Movie updatedMovie = updatedMovie().build();

        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(movie));
        when(movieRepository.existsByTitle(UPDATED_TITLE)).thenReturn(false);
        when(movieRepository.save(any())).thenReturn(updatedMovie);

        MovieResponse responseWithUpdatedMovie = movieService.updateMovie(MOVIE_ID, request);

        assertMovieResponse(updatedMovie, responseWithUpdatedMovie);

        ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);

        verify(movieRepository).save(movieCaptor.capture());

        Movie savedMovie = movieCaptor.getValue();

        assertAll(
                () -> assertEquals(UPDATED_TITLE, savedMovie.getTitle()),
                () -> assertEquals(UPDATED_DESCRIPTION, savedMovie.getDescription()),
                () -> assertEquals(UPDATED_REQUIRED_AGE, savedMovie.getRequiredAge()),
                () -> assertEquals(UPDATED_DURATION_MINUTES, savedMovie.getDuration().toMinutes())
        );
    }

    @Test
    void updateMovie_shouldUpdateWithoutCheckingTitleConflict_whenTitleDidNotChange() {
        UpdateMovieRequest request = anyUpdateRequest()
                .title(TITLE)
                .build();

        Movie movie = anyMovie().build();

        Movie updatedMovie = updatedMovie()
                .title(TITLE)
                .build();

        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any())).thenReturn(updatedMovie);

        MovieResponse response = movieService.updateMovie(MOVIE_ID, request);

        assertMovieResponse(updatedMovie, response);

        verify(movieRepository, never()).existsByTitle(TITLE);
    }

    @Test
    void updateMovie_shouldThrowException_whenExistsMovieWithSameTitle() {
        Movie movie = anyMovie().build();
        UpdateMovieRequest request = anyUpdateRequest().build();

        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(movie));
        when(movieRepository.existsByTitle(UPDATED_TITLE)).thenReturn(true);

        assertThrows(
                MovieAlreadyExistsException.class,
                () -> movieService.updateMovie(MOVIE_ID, request)
        );
    }

}