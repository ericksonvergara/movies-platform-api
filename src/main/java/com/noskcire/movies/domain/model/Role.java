package com.noskcire.movies.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles", schema = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String name;
}
