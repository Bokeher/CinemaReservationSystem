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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void shouldFindMovieById() {
        Movie movie = getSavedMovie();

        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(movie));

        MovieResponse movieResponse = movieService.findById(MOVIE_ID);

        assertMovieResponse(movieResponse, MOVIE_ID, TITLE, DESCRIPTION, REQUIRED_AGE, DURATION_MINUTES);
    }

    @Test
    void shouldThrowWhenMovieByIdNotFound() {
        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.empty());

        MovieNotFoundException exception = assertThrows(
                MovieNotFoundException.class,
                () -> movieService.findById(MOVIE_ID)
        );

        assertEquals("Movie not found with id: 1", exception.getMessage());
        verify(movieRepository).findById(MOVIE_ID);
    }

    @Test
    void shouldFindAllMovies() {
        when(movieRepository.findAll()).thenReturn(List.of(
                getSavedMovie(),
                getUpdatedMovie()
        ));

        List<MovieResponse> movieResponses = movieService.findAll();

        assertEquals(2, movieResponses.size());
        assertSavedMovieResponse(movieResponses.get(0));
        assertUpdatedMovieResponse(movieResponses.get(1));
        verify(movieRepository).findAll();
    }

    @Test
    void shouldFindOneMovieByTitleIgnoreCase() {
        String titlePartial = TITLE.substring(0, 3).toLowerCase();

        when(movieRepository.findByTitleContainingIgnoreCase(titlePartial))
                .thenReturn(List.of(getSavedMovie()));

        List<MovieResponse> movieResponses = movieService.findByTitleIgnoreCase(titlePartial);

        assertEquals(1, movieResponses.size());
        assertSavedMovieResponse(movieResponses.get(0));

        verify(movieRepository).findByTitleContainingIgnoreCase(titlePartial);
    }

    @Test
    void shouldReturnAllMoviesWhenFindWithoutTitle() {
        when(movieRepository.findAll()).thenReturn(List.of(
                getSavedMovie(),
                getUpdatedMovie()
        ));

        List<MovieResponse> movieResponses = movieService.find(null);

        assertEquals(2, movieResponses.size());
        assertSavedMovieResponse(movieResponses.get(0));
        assertUpdatedMovieResponse(movieResponses.get(1));
        verify(movieRepository).findAll();
    }

    @Test
    void shouldReturnMoviesByTitleWhenFindWithTitle() {
        when(movieRepository.findByTitleContainingIgnoreCase(TITLE))
                .thenReturn(List.of(getSavedMovie()));

        List<MovieResponse> movieResponses = movieService.find(TITLE);

        assertEquals(1, movieResponses.size());
        assertSavedMovieResponse(movieResponses.get(0));

        verify(movieRepository).findByTitleContainingIgnoreCase(TITLE);
    }

    @Test
    void shouldCreateMovie() {
        CreateMovieRequest request = getCreateRequest();
        Movie savedMovie = getSavedMovie();

        when(movieRepository.existsByTitle(TITLE)).thenReturn(false);
        when(movieRepository.save(any())).thenReturn(savedMovie);

        MovieResponse response = movieService.createMovie(request);

        assertSavedMovieResponse(response);

        ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);

        verify(movieRepository).existsByTitle(TITLE);
        verify(movieRepository).save(movieCaptor.capture());

        Movie persistedMovie = movieCaptor.getValue();

        assertAll(
                () -> assertEquals(TITLE, persistedMovie.getTitle()),
                () -> assertEquals(DESCRIPTION, persistedMovie.getDescription()),
                () -> assertEquals(REQUIRED_AGE, persistedMovie.getRequiredAge()),
                () -> assertEquals(DURATION_MINUTES, persistedMovie.getDuration().toMinutes())
        );
    }

    @Test
    void shouldThrowWhenCreatingMovieWithExistingTitle() {
        CreateMovieRequest request = getCreateRequest();

        when(movieRepository.existsByTitle(TITLE)).thenReturn(true);

        MovieAlreadyExistsException exception = assertThrows(
                MovieAlreadyExistsException.class,
                () ->  movieService.createMovie(request)
        );

        assertEquals("Movie with title '" + TITLE + "' already exists", exception.getMessage());
        verify(movieRepository).existsByTitle(TITLE);
        verify(movieRepository, never()).save(any());
    }

    @Test
    void shouldDeleteMovie() {
        Movie movie = getSavedMovie();

        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(movie));

        movieService.deleteMovie(MOVIE_ID);

        verify(movieRepository).findById(MOVIE_ID);
        verify(movieRepository).delete(movie);
    }

    @Test
    void shouldThrowWhenDeletingMovieThatDoesNotExist() {
        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.empty());

        MovieNotFoundException exception = assertThrows(
                MovieNotFoundException.class,
                () -> movieService.deleteMovie(MOVIE_ID)
        );

        assertEquals("Movie not found with id: " + MOVIE_ID, exception.getMessage());
        verify(movieRepository).findById(MOVIE_ID);
        verify(movieRepository, never()).delete(any());
    }

    @Test
    void shouldUpdateMovie() {
        UpdateMovieRequest request = getUpdateRequest();

        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(getSavedMovie()));
        when(movieRepository.existsByTitle(UPDATED_TITLE)).thenReturn(false);
        when(movieRepository.save(any())).thenReturn(getUpdatedMovie());

        MovieResponse response = movieService.updateMovie(MOVIE_ID, request);

        assertUpdatedMovieResponse(response);

        ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);

        verify(movieRepository).findById(MOVIE_ID);
        verify(movieRepository).existsByTitle(UPDATED_TITLE);
        verify(movieRepository).save(movieCaptor.capture());

        Movie updatedMovie = movieCaptor.getValue();

        assertAll(
                () -> assertEquals(UPDATED_TITLE, updatedMovie.getTitle()),
                () -> assertEquals(UPDATED_DESCRIPTION, updatedMovie.getDescription()),
                () -> assertEquals(UPDATED_REQUIRED_AGE, updatedMovie.getRequiredAge()),
                () -> assertEquals(UPDATED_DURATION_MINUTES, updatedMovie.getDuration().toMinutes())
        );
    }

    @Test
    void shouldUpdateMovieWithoutCheckingTitleConflictWhenTitleDidNotChange() {
        UpdateMovieRequest request = getUpdateRequestSameTitle();

        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(getSavedMovie()));
        when(movieRepository.save(any())).thenReturn(getUpdatedMovieSameTitle());

        MovieResponse response = movieService.updateMovie(MOVIE_ID, request);

        assertUpdatedMovieResponseSameTitle(response);

        verify(movieRepository).findById(MOVIE_ID);
        verify(movieRepository, never()).existsByTitle(TITLE);
        verify(movieRepository).save(any());
    }

    @Test
    void shouldThrowWhenUpdatingMissingMovie() {
        UpdateMovieRequest request = getUpdateRequestSameTitle();

        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.empty());

        MovieNotFoundException exception = assertThrows(
                MovieNotFoundException.class,
                () -> movieService.updateMovie(MOVIE_ID, request)
        );

        assertEquals("Movie not found with id: " + MOVIE_ID, exception.getMessage());
        verify(movieRepository).findById(MOVIE_ID);
        verify(movieRepository, never()).existsByTitle(TITLE);
        verify(movieRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenUpdatingMovieWithExistingTitle() {
        Movie movie = getSavedMovie();
        UpdateMovieRequest request = getUpdateRequest();

        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(movie));
        when(movieRepository.existsByTitle(UPDATED_TITLE)).thenReturn(true);

        MovieAlreadyExistsException exception = assertThrows(
                MovieAlreadyExistsException.class,
                () -> movieService.updateMovie(MOVIE_ID, request)
        );

        assertEquals("Movie with title '" + UPDATED_TITLE + "' already exists", exception.getMessage());
        verify(movieRepository).findById(MOVIE_ID);
        verify(movieRepository).existsByTitle(UPDATED_TITLE);
        verify(movieRepository, never()).save(any());
    }

    private void assertSavedMovieResponse(MovieResponse movieResponse) {
        assertMovieResponse(movieResponse, MOVIE_ID, TITLE, DESCRIPTION, REQUIRED_AGE, DURATION_MINUTES);
    }
    private void assertUpdatedMovieResponse(MovieResponse movieResponse) {
        assertMovieResponse(movieResponse, UPDATED_MOVIE_ID, UPDATED_TITLE, UPDATED_DESCRIPTION, UPDATED_REQUIRED_AGE, UPDATED_DURATION_MINUTES);
    }
    private void assertUpdatedMovieResponseSameTitle(MovieResponse movieResponse) {
        assertMovieResponse(movieResponse, UPDATED_MOVIE_ID, TITLE, UPDATED_DESCRIPTION, UPDATED_REQUIRED_AGE, UPDATED_DURATION_MINUTES);
    }

    private void assertMovieResponse(
            MovieResponse response,
            Long id,
            String title,
            String description,
            int requiredAge,
            int durationMinutes
    ) {
        assertAll(
                () -> assertEquals(id, response.getId()),
                () -> assertEquals(title, response.getTitle()),
                () -> assertEquals(description, response.getDescription()),
                () -> assertEquals(requiredAge, response.getRequiredAge()),
                () -> assertEquals(durationMinutes, response.getDurationMinutes())
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

    private Movie getUpdatedMovie() {
        return new Movie(
                UPDATED_MOVIE_ID,
                UPDATED_TITLE,
                UPDATED_DESCRIPTION,
                UPDATED_REQUIRED_AGE,
                Duration.ofMinutes(UPDATED_DURATION_MINUTES)
        );
    }
    private Movie getUpdatedMovieSameTitle() {
        return new Movie(
                UPDATED_MOVIE_ID,
                TITLE,
                UPDATED_DESCRIPTION,
                UPDATED_REQUIRED_AGE,
                Duration.ofMinutes(UPDATED_DURATION_MINUTES)
        );
    }

    private Movie getSavedMovie() {
        return new Movie(
                MOVIE_ID,
                TITLE,
                DESCRIPTION,
                REQUIRED_AGE,
                Duration.ofMinutes(DURATION_MINUTES)
        );
    }
}
