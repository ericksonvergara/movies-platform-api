package com.noskcire.movies.infrastructure.adapter.input.rest;

import com.noskcire.movies.application.dto.movie.MovieResponse;
import com.noskcire.movies.application.dto.response.ApiResponse;
import com.noskcire.movies.application.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/catalog")
@RestController
@RequiredArgsConstructor
public class CatalogController {

    private final MovieService movieService;

    @Operation(summary = "Obtener todas las películas", description = "Devuelve el listado completo de películas del catálogo")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado de películas obtenido correctamente")
    })
    @GetMapping("movies")
    public List<MovieResponse> getAllMovies() {
        return movieService.getAllMovies();
    }

    @Operation(summary = "Obtener película por ID", description = "Devuelve los detalles de una película específica por su identificador")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Película obtenida correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Película no encontrada")
    })
    @GetMapping("/movies/{id}")
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

}
