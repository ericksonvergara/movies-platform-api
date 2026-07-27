package com.noskcire.movies.infrastructure.adapter.input.rest;

import com.noskcire.movies.application.dto.movie.CreateMovieRequest;
import com.noskcire.movies.application.dto.movie.MovieResponse;
import com.noskcire.movies.application.dto.movie.UpdateMovieRequest;
import com.noskcire.movies.application.dto.response.ApiResponse;
import com.noskcire.movies.application.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Crear una nueva película", description = "Registra una nueva película en el catálogo")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Película creada correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de la película inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponse createMovie(@Valid @RequestBody CreateMovieRequest createMovieRequest){
        return movieService.createMovie(createMovieRequest);
    }

    @Operation(summary = "Obtener todas las películas", description = "Devuelve el listado completo de películas del catálogo")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado de películas obtenido correctamente")
    })
    @GetMapping
    public List<MovieResponse> getAllMovies(){

        return movieService.getAllMovies();
    }

    @Operation(summary = "Obtener película por ID", description = "Devuelve los detalles de una película específica por su identificador")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Película obtenida correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Película no encontrada")
    })
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

    @Operation(summary = "Actualizar película", description = "Actualiza los datos de una película existente")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Película actualizada correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Película no encontrada")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MovieResponse updateMovie(
            @PathVariable Long id,
            @Valid
            @RequestBody UpdateMovieRequest updateMovieRequest
            ) {
        return movieService.updateMovie(id, updateMovieRequest);
    }

    @Operation(summary = "Eliminar película", description = "Elimina una película del catálogo por su identificador")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Película eliminada correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Película no encontrada")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMovie(
            @PathVariable Long id
    ) {
        movieService.deleteMovie(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener películas paginadas", description = "Devuelve el listado de películas con paginación y ordenamiento")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado paginado obtenido correctamente")
    })
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

    @Operation(summary = "Buscar películas", description = "Busca películas por título, género y/o año de lanzamiento con paginación")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Resultados de búsqueda obtenidos correctamente")
    })
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
