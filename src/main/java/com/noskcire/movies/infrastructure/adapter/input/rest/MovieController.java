package com.noskcire.movies.infrastructure.adapter.input.rest;

import com.noskcire.movies.application.dto.movie.CreateMovieRequest;
import com.noskcire.movies.application.dto.movie.MovieResponse;
import com.noskcire.movies.application.dto.movie.UpdateMovieRequest;
import com.noskcire.movies.application.dto.response.ApiResponse;
import com.noskcire.movies.application.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponse createMovie(@Valid @RequestBody CreateMovieRequest createMovieRequest){
        return movieService.createMovie(createMovieRequest);
    }

    @GetMapping
    public List<MovieResponse> getAllMovies(){

        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MovieResponse>> getMovieById(
            @PathVariable Long id
    ) {
        MovieResponse movie = movieService.getMovieById(id);

        ApiResponse<MovieResponse> response =
                new ApiResponse<>(
                        true,
                        "Película obtenida correctamente.",
                        movie,
                        LocalDateTime.now()
                );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MovieResponse updateMovie(
            @PathVariable Long id,
            @Valid
            @RequestBody UpdateMovieRequest updateMovieRequest
            ) {
        return movieService.updateMovie(id, updateMovieRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMovie(
            @PathVariable Long id
    ) {
        movieService.deleteMovie(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/paginated")
    public Page<MovieResponse> getMoviesPaginated(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size,

            @RequestParam(defaultValue = "id")
            String sort,

            @RequestParam(defaultValue = "asc")
            String direction
    ){
        return movieService.getMoviesPaginated(
                page,
                size,
                sort,
                direction
        );
    }

    @GetMapping("/search")
    public Page<MovieResponse> searchMovies(
            @RequestParam(required = false)
            String title,

            @RequestParam (required = false)
            String gender,

            @RequestParam(required = false)
            Integer releaseYear,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size
    ) {
        return movieService.searchMovies(
                title,
                gender,
                releaseYear,
                page,
                size
        );
    }
}
