package com.noskcire.movies.application.service;

import com.noskcire.movies.application.dto.movie.CreateMovieRequest;
import com.noskcire.movies.application.dto.movie.MovieResponse;
import com.noskcire.movies.application.dto.movie.UpdateMovieRequest;
import com.noskcire.movies.domain.exception.ResourceNotFoundException;
import com.noskcire.movies.domain.model.Movie;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.MovieRepository;
import com.noskcire.movies.infrastructure.specification.GenericSpecification;
import com.noskcire.movies.infrastructure.specification.MovieSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
        movie.setStock(request.stock());
        movie.setRentalPrice(request.rentalPrice());
        movieRepository.save(movie);

        return mapToResponse(movie);
    }

    public void deleteMovie(Long id){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Pelicula no encontrada."
                        )
                );
        movie.setDeleted(true);
        movieRepository.save(movie);
    }

    public Page<MovieResponse> getMoviesPaginated(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        return movieRepository
                .findAll(pageable)
                .map(this::mapToResponse);
    }

    public Page<MovieResponse> searchMovies(
            String title,
            String gender,
            Integer releaseYear,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Movie> spec =
                Specification
                        .where(
                                GenericSpecification.<Movie>contains(
                                        "title",
                                        title
                                )
                        )
                        .and(
                                GenericSpecification.equalsTo(
                                        "gender",
                                        gender
                                )
                        )
                        .and(
                                GenericSpecification.equalsTo(
                                        "releaseYear",
                                        releaseYear
                                )
                        );

        return movieRepository
                .findAll(spec, pageable)
                .map(this::mapToResponse);
    }
}
