package com.example.amlang.entity;

import com.example.amlang.enums.RevenueType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "ad_revenue")
@Data
public class AdRevenue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long revenueId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monetization_id", nullable = false)
    private VideoMonetization monetization;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    private Ad ad;

    @NotNull
    @Column(columnDefinition = "DECIMAL(15,2) DEFAULT 0.00", nullable = false)
    private BigDecimal revenueAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RevenueType revenueType;

    @NotNull
    @Column(columnDefinition = "INT DEFAULT 0", nullable = false)
    private Integer impressions;

    @NotNull
    @Column(columnDefinition = "INT DEFAULT 0", nullable = false)
    private Integer clicks;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}