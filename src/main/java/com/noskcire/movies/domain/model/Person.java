package com.noskcire.movies.domain.model;

import com.noskcire.movies.domain.audit.BaseAuditEntity;
import com.noskcire.movies.domain.enums.PersonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "persons")
@SQLDelete(sql = "UPDATE persons SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String names;
    private String lastNames;

    @Column(unique = true)
    private String document;

    private String phone;

    @Column(unique = true)
    private String email;

    private String address;

    private LocalDate dateBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PersonType type;

    @Column(nullable = false)
    private boolean deleted = false;
}
