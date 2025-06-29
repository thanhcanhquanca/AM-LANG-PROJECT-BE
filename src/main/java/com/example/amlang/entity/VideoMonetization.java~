package com.example.amlang.entity;

import com.example.amlang.enums.MonetizationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "video_monetization")
@Data
public class VideoMonetization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long monetizationId;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false, unique = true)
    private Video video;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('PENDING', 'APPROVED', 'REJECTED', 'DISABLED') DEFAULT 'PENDING'")
    private MonetizationStatus status = MonetizationStatus.PENDING;

    @NotNull
    @Column(columnDefinition = "DECIMAL(15,2) DEFAULT 0.00", nullable = false)
    private BigDecimal totalRevenue;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}
