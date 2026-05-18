package com.noskcire.movies.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "persons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String names;
    private String lastNames;

    @Column(unique = true)
    private String document;

    private String phone;

    @Column(unique = true)
    private String email;

    private String address;

    private LocalDate dateBirth;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
