package com.example.amlang.entity;

import com.example.amlang.enums.PenaltyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "copyright_violations")
@Data
public class CopyrightViolation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long violationId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @NotNull
    @Column(columnDefinition = "INT DEFAULT 1", nullable = false)
    private Integer violationCount;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('NONE', 'WARNING', 'TEMP_BAN', 'PERM_BAN', 'FINE') DEFAULT 'NONE'")
    private PenaltyType penaltyApplied = PenaltyType.NONE;

    @Column(columnDefinition = "DECIMAL(15,2) DEFAULT 0.00")
    private BigDecimal fineAmount;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}