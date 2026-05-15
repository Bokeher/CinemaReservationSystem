package com.bokeher.cinema.CinemaReservationSystem.movie;

import com.bokeher.cinema.CinemaReservationSystem.movie.dto.CreateMovieRequest;
import com.bokeher.cinema.CinemaReservationSystem.movie.dto.MovieResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/{id}")
    public MovieResponse getMovie(@PathVariable Long id) {
        return movieService.findById(id);
    }

    @GetMapping
    public List<MovieResponse> getMovies(@RequestParam(required = false) String title) {
        return movieService.find(title);
    }

    @PostMapping
    public MovieResponse createMovie(@RequestBody CreateMovieRequest request) {
        return movieService.createMovie(request);
    }
}
