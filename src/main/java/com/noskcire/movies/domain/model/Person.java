package com.noskcire.movies.domain.model;

import com.noskcire.movies.domain.audit.BaseAuditEntity;
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
public class Person extends BaseAuditEntity {

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

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

}
