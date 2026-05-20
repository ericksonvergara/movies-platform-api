package com.noskcire.movies.application.service;

import com.noskcire.movies.application.dto.movie.CreateMovieRequest;
import com.noskcire.movies.application.dto.movie.MovieResponse;
import com.noskcire.movies.application.dto.movie.UpdateMovieRequest;
import com.noskcire.movies.domain.exception.ResourceNotFoundException;
import com.noskcire.movies.domain.model.Movie;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieResponse createMovie(
            CreateMovieRequest createMovieRequest
    ) {
        Movie movie = Movie.builder()
                .title(createMovieRequest.title())
                .description(createMovieRequest.description())
                .gender(createMovieRequest.gender())
                .releaseYear(createMovieRequest.releaseYear())
                .stock(createMovieRequest.stock())
                .rentalPrice(createMovieRequest.rentalPrice())
                .build();

        Movie saveMovie =
                movieRepository.save(movie);

        return mapToResponse(saveMovie);

    }

    private MovieResponse mapToResponse(
            Movie movie
    ) {
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getGender(),
                movie.getReleaseYear(),
                movie.getStock(),
                movie.getRentalPrice(),
                movie.getCreatedAt(),
                movie.getUpdatedAt()
        );
    }

    public List<MovieResponse> getAllMovies(){
        List<Movie> movies =
                movieRepository.findAll();

        return movies
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MovieResponse getMovieById(
            Long id
    ) {
        Movie movie = movieRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Pelicula no encontrada."
                        )
                );
        return mapToResponse(movie);
    }

    public MovieResponse updateMovie(
            Long id,
            UpdateMovieRequest request
    ) {
        Movie movie =
                movieRepository.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Pelicula no encontrada"
                                )
                        );
        movie.setTitle(request.title());
        movie.setDescription(request.description());
        movie.setReleaseYear(request.releaseYear());
        movie.setRentalPrice(request.rentalPrice());
        movieRepository.save(movie);

        return mapToResponse(movie);
    }
}
