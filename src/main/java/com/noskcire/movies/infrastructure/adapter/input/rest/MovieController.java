package com.noskcire.movies.infrastructure.adapter.input.rest;

import com.noskcire.movies.application.dto.movie.CreateMovieRequest;
import com.noskcire.movies.application.dto.movie.MovieResponse;
import com.noskcire.movies.application.dto.movie.UpdateMovieRequest;
import com.noskcire.movies.application.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponse createMovie(@RequestBody CreateMovieRequest createMovieRequest){
        return movieService.createMovie(createMovieRequest);
    }

    @GetMapping
    public List<MovieResponse> getAllMovies(){
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public MovieResponse getMovieById(
            @PathVariable Long id
    ) {
        return movieService.getMovieById(id);
    }

    @PutMapping("/{id}")
    public MovieResponse updateMovie(
            @PathVariable Long id,
            @RequestBody UpdateMovieRequest updateMovieRequest
            ) {
        return movieService.updateMovie(id, updateMovieRequest);
    }
}
