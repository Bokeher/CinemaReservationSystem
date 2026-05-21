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

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    private static final Long MOVIE_ID = 1L;
    private static final String TITLE = "Inception";
    private static final String DESCRIPTION = "A mind-bending sci-fi thriller";
    private static final int REQUIRED_AGE = 13;
    private static final int DURATION_MINUTES = 148;

    private static final Long UPDATED_MOVIE_ID = 2L;
    private static final String UPDATED_TITLE = "Interstellar";
    private static final String UPDATED_DESCRIPTION = "A sci-fi story about space and time";
    private static final int UPDATED_REQUIRED_AGE = 16;
    private static final int UPDATED_DURATION_MINUTES = 169;

    @Mock
    private MovieRepository movieRepository;

    private MovieService movieService;

    @BeforeEach
    void setUp() {
        movieService = new MovieService(movieRepository, new MovieMapper());
    }

    @Test
    void getById_shouldReturnMovie_whenMovieExists() {
        Movie movie = createExampleMovie();
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
        Movie movie = createExampleMovie();
        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(movie));

        MovieResponse response = movieService.findById(MOVIE_ID);

        assertResponseEqualsMovie(movie, response);
    }

    @Test
    void findAll_shouldReturnListOfResponses_whenMoviesExist() {
        Movie movie = createExampleMovie();
        Movie movie2 = createExampleMovie2();

        when(movieRepository.findAll()).thenReturn(List.of(movie, movie2));

        List<MovieResponse> movieResponses = movieService.findAll();

        assertEquals(2, movieResponses.size());
        assertResponseEqualsMovie(movie, movieResponses.get(0));
        assertResponseEqualsMovie(movie2, movieResponses.get(1));
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoMovieExist() {
        when(movieRepository.findAll()).thenReturn(List.of());

        List<MovieResponse> response = movieService.findAll();

        assertTrue(response.isEmpty());
    }

    @Test
    void find_shouldReturnAllMovies_whenTitleIsNull() {
        Movie movie = createExampleMovie();
        Movie movie2 = createExampleMovie2();

        when(movieRepository.findAll()).thenReturn(List.of(movie, movie2));

        List<MovieResponse> movieResponses = movieService.find(null);

        assertEquals(2, movieResponses.size());
        assertResponseEqualsMovie(movie, movieResponses.get(0));
        assertResponseEqualsMovie(movie2, movieResponses.get(1));
        verify(movieRepository).findAll();
    }

    @Test
    void find_shouldReturnOneMovie_whenTitleNotNull() {
        Movie movie = createExampleMovie();

        when(movieRepository.findByTitleContainingIgnoreCase(TITLE))
                .thenReturn(List.of(movie));

        List<MovieResponse> movieResponses = movieService.find(TITLE);

        assertEquals(1, movieResponses.size());
        assertResponseEqualsMovie(movie, movieResponses.get(0));

        verify(movieRepository).findByTitleContainingIgnoreCase(TITLE);
    }

    @Test
    void createMovie_shouldCreateMovie_whenTitleDoesNotExist() {
        CreateMovieRequest request = getCreateRequest();
        Movie movie = createExampleMovie();

        when(movieRepository.existsByTitle(TITLE)).thenReturn(false);
        when(movieRepository.save(any())).thenReturn(movie);

        MovieResponse response = movieService.createMovie(request);

        assertResponseEqualsMovie(movie, response);

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
        CreateMovieRequest request = getCreateRequest();

        when(movieRepository.existsByTitle(TITLE)).thenReturn(true);

        assertThrows(
                MovieAlreadyExistsException.class,
                () ->  movieService.createMovie(request)
        );
    }

    @Test
    void deleteMovie_shouldDeleteMovie() {
        Movie movie = createExampleMovie();
        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(movie));

        movieService.deleteMovie(MOVIE_ID);

        verify(movieRepository).delete(movie);
    }

    @Test
    void updateMovie_shouldUpdateMovie_whenCompleteRequest() {
        UpdateMovieRequest request = getUpdateRequest();
        Movie movie = createExampleMovie();
        Movie updatedMovie = createExampleMovie2();

        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(movie));
        when(movieRepository.existsByTitle(UPDATED_TITLE)).thenReturn(false);
        when(movieRepository.save(any())).thenReturn(updatedMovie);

        MovieResponse responseWithUpdatedMovie = movieService.updateMovie(MOVIE_ID, request);

        assertResponseEqualsMovie(updatedMovie, responseWithUpdatedMovie);

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
        UpdateMovieRequest request = getUpdateRequestSameTitle();
        Movie movie = createExampleMovie();
        Movie updatedMovie = createExampleMovie2();
        updatedMovie.setTitle(movie.getTitle());

        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any())).thenReturn(updatedMovie);

        MovieResponse response = movieService.updateMovie(MOVIE_ID, request);

        assertResponseEqualsMovie(updatedMovie, response);

        verify(movieRepository, never()).existsByTitle(TITLE);
    }

    @Test
    void updateMovie_shouldThrowException_whenExistsMovieWithSameTitle() {
        Movie movie = createExampleMovie();
        UpdateMovieRequest request = getUpdateRequest();

        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(movie));
        when(movieRepository.existsByTitle(UPDATED_TITLE)).thenReturn(true);

        assertThrows(
                MovieAlreadyExistsException.class,
                () -> movieService.updateMovie(MOVIE_ID, request)
        );
    }

    private CreateMovieRequest getCreateRequest() {
        return CreateMovieRequest.builder()
                .title(TITLE)
                .description(DESCRIPTION)
                .requiredAge(REQUIRED_AGE)
                .durationMinutes(DURATION_MINUTES)
                .build();
    }

    private UpdateMovieRequest getUpdateRequest() {
        return UpdateMovieRequest.builder()
                .title(UPDATED_TITLE)
                .description(UPDATED_DESCRIPTION)
                .requiredAge(UPDATED_REQUIRED_AGE)
                .durationMinutes(UPDATED_DURATION_MINUTES)
                .build();
    }

    private UpdateMovieRequest getUpdateRequestSameTitle() {
        return UpdateMovieRequest.builder()
                .title(TITLE)
                .description(UPDATED_DESCRIPTION)
                .requiredAge(UPDATED_REQUIRED_AGE)
                .durationMinutes(UPDATED_DURATION_MINUTES)
                .build();
    }

    private Movie createExampleMovie2() {
        return createMovie(
                UPDATED_MOVIE_ID,
                UPDATED_TITLE,
                UPDATED_DESCRIPTION,
                UPDATED_REQUIRED_AGE,
                UPDATED_DURATION_MINUTES
        );
    }

    private Movie createExampleMovie() {
        return createMovie(MOVIE_ID, TITLE, DESCRIPTION, REQUIRED_AGE, DURATION_MINUTES);
    }

    private Movie createMovie(long id, String title, String description, int requiredAge, int durationMinutes) {
        return Movie.builder()
                .id(id)
                .title(title)
                .description(description)
                .requiredAge(requiredAge)
                .duration(Duration.ofMinutes(durationMinutes))
                .build();
    }

    private void assertMovie(Movie expected, Movie actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getRequiredAge(), actual.getRequiredAge());
        assertEquals(expected.getDuration(), actual.getDuration());
    }

    private void assertResponseEqualsMovie(Movie expectedMovie, MovieResponse actualResponse) {
        assertEquals(expectedMovie.getId(), actualResponse.getId());
        assertEquals(expectedMovie.getTitle(), actualResponse.getTitle());
        assertEquals(expectedMovie.getDescription(), actualResponse.getDescription());
        assertEquals(expectedMovie.getRequiredAge(), actualResponse.getRequiredAge());
        assertEquals(expectedMovie.getDuration().toMinutes(), actualResponse.getDurationMinutes());
    }
}
