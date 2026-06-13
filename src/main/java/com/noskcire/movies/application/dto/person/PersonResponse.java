package com.noskcire.movies.application.dto.person;

import com.noskcire.movies.domain.enums.PersonType;

import java.time.LocalDate;

public record PersonResponse(

        Long id,

        String names,

        String lastNames,

        String document,

        String phone,

        String email,

        String address,

        LocalDate dateBirth,

        PersonType type
) {
}
