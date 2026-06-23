package com.noskcire.movies.infrastructure.adapter.input.rest;

import com.noskcire.movies.application.dto.person.CreatePersonRequest;
import com.noskcire.movies.application.dto.person.PersonResponse;
import com.noskcire.movies.application.dto.person.UpdatePersonRequest;
import com.noskcire.movies.application.service.PersonService;
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

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<PersonResponse> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PersonResponse getPersonById(
            @PathVariable Long id
    ) {
        return personService.getPersonById(id);
    }

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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(
            @PathVariable Long id
    ) {
        personService.deletePerson(id);
    }

}
