package com.noskcire.movies.infrastructure.adapter.input.rest;

import com.noskcire.movies.application.dto.person.CreatePersonRequest;
import com.noskcire.movies.application.dto.person.PersonResponse;
import com.noskcire.movies.application.dto.person.UpdatePersonRequest;
import com.noskcire.movies.application.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @Operation(summary = "Crear una nueva persona", description = "Registra una nueva persona (cliente) en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Persona creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la persona inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public PersonResponse createPerson(
            @Valid
            @RequestBody
            CreatePersonRequest createPersonRequest
    ) {
        return personService.createPerson(createPersonRequest);
    }

    @Operation(summary = "Obtener todas las personas", description = "Devuelve el listado completo de personas registradas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de personas obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<PersonResponse> getAllPersons() {
        return personService.getAllPersons();
    }

    @Operation(summary = "Obtener persona por ID", description = "Devuelve los datos de una persona específica por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Persona encontrada"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PersonResponse getPersonById(
            @PathVariable Long id
    ) {
        return personService.getPersonById(id);
    }

    @Operation(summary = "Actualizar persona", description = "Actualiza los datos de una persona existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Persona actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PersonResponse updatePerson(
            @PathVariable Long id,
            @Valid
            @RequestBody
            UpdatePersonRequest updatePersonRequest
    ){
        return personService.updatePerson(id,updatePersonRequest);
    }

    @Operation(summary = "Eliminar persona", description = "Elimina una persona del sistema por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Persona eliminada correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(
            @PathVariable Long id
    ) {
        personService.deletePerson(id);
    }

}
