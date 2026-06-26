package com.noskcire.movies.domain.model;

import com.noskcire.movies.domain.audit.BaseAuditEntity;
import com.noskcire.movies.domain.enums.LateFeeStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "late_fees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LateFee extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "rental_id", nullable = false, unique = true)
    private Rental rental;

    @Column(nullable = false)
    private Long daysLate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal dailyAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LateFeeStatus status;
}
