package com.noskcire.movies.application.service;

import com.noskcire.movies.application.dto.person.CreatePersonRequest;
import com.noskcire.movies.application.dto.person.PersonResponse;
import com.noskcire.movies.application.dto.person.UpdatePersonRequest;
import com.noskcire.movies.domain.enums.PersonType;
import com.noskcire.movies.domain.exception.BadRequestException;
import com.noskcire.movies.domain.exception.ResourceNotFoundException;
import com.noskcire.movies.domain.model.Person;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public PersonResponse createPerson(
            CreatePersonRequest createPersonRequest
    ) {
        if (personRepository.existsByDocument(createPersonRequest.document())) {
            throw new BadRequestException(
                    "Ya existe una persona con el mismo documento."
            );
        }

        Person person = Person.builder()
                .names(createPersonRequest.names())
                .lastNames(createPersonRequest.lastNames())
                .document(createPersonRequest.document())
                .phone(createPersonRequest.phone())
                .email(createPersonRequest.email())
                .address(createPersonRequest.address())
                .dateBirth(createPersonRequest.dateBirth())
                .type(PersonType.CLIENT)
                .build();

        personRepository.save(person);
        return mapToResponse(person);

    }

    public List<PersonResponse> getAllPersons() {

        return personRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PersonResponse getPersonById(
            Long id
    ) {

        Person person = personRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Persona no encontrada."
                        )
                );

        return mapToResponse(person);
    }

    public PersonResponse updatePerson(
            Long id,
            UpdatePersonRequest updatePersonRequest
    ) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada."));

        // Validar duplicado solo si el campo viene y es diferente al actual
        if (updatePersonRequest.document() != null &&
                !person.getDocument().equals(updatePersonRequest.document()) &&
                personRepository.existsByDocument(updatePersonRequest.document())) {
            throw new BadRequestException("Ya existe una persona con el mismo documento.");
        }

        if (updatePersonRequest.email() != null &&
                !person.getEmail().equals(updatePersonRequest.email()) &&
                personRepository.existsByEmail(updatePersonRequest.email())) {
            throw new BadRequestException("Ya existe una persona con el mismo correo.");
        }

        // Actualiza solo los campos que llegan
        if (updatePersonRequest.names() != null)     person.setNames(updatePersonRequest.names());
        if (updatePersonRequest.lastNames() != null) person.setLastNames(updatePersonRequest.lastNames());
        if (updatePersonRequest.document() != null)  person.setDocument(updatePersonRequest.document());
        if (updatePersonRequest.phone() != null)     person.setPhone(updatePersonRequest.phone());
        if (updatePersonRequest.email() != null)     person.setEmail(updatePersonRequest.email());
        if (updatePersonRequest.address() != null)   person.setAddress(updatePersonRequest.address());
        if (updatePersonRequest.dateBirth() != null) person.setDateBirth(updatePersonRequest.dateBirth());

        personRepository.save(person);

        return mapToResponse(person);
    }

    public void deletePerson(
            Long id
    ){
        Person person = personRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Persona no encontrada."
                        )
                );
        personRepository.delete(person);
    }

    private PersonResponse mapToResponse(Person person) {
        return new PersonResponse(
                person.getId(),
                person.getNames(),
                person.getLastNames(),
                person.getDocument(),
                person.getPhone(),
                person.getEmail(),
                person.getAddress(),
                person.getDateBirth(),
                person.getType()
        );
    }
}
