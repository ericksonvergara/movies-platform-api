package com.noskcire.movies.domain.model;

import com.noskcire.movies.domain.audit.BaseAuditEntity;
import com.noskcire.movies.domain.enums.LateFeeStatus;
import com.noskcire.movies.domain.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "late_fees", schema = "movies")
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

    @Column
    private Long daysLate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal dailyAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LateFeeStatus status;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 30)
    private PaymentMethod paymentMethod;

    @Column(length = 500)
    private String observations;
}
