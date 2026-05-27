package com.noskcire.movies.domain.model;

import com.noskcire.movies.domain.audit.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movies")
@SQLDelete(sql = """
        UPDATE movies
        SET deleted = true
        WHERE id = ?
        """)
@SQLRestriction("deleted = false")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String gender;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "rental_price", precision = 10, scale = 2)
    private BigDecimal rentalPrice;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

    }

    @PreUpdate
    public void preUpdate() {

        this.updatedAt = LocalDateTime.now();

    }

    @Column(nullable = false)
    private boolean deleted = false;
}
