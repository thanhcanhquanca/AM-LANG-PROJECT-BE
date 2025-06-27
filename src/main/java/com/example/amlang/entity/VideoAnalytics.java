package com.example.amlang.entity;

import com.example.amlang.enums.PeriodType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "video_analytics", indexes = {
        @Index(name = "idx_video_id_period", columnList = "video_id, period_type, period_value", unique = true)
})
@Data
public class VideoAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long analyticsId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeriodType periodType;

    @NotNull
    @Size(max = 10)
    @Column(nullable = false)
    private String periodValue;

    @NotNull
    @Column(columnDefinition = "INT DEFAULT 0", nullable = false)
    private Integer viewCount;

    @NotNull
    @Column(columnDefinition = "INT DEFAULT 0", nullable = false)
    private Integer likeCount;

    @NotNull
    @Column(columnDefinition = "DECIMAL(15,2) DEFAULT 0.00", nullable = false)
    private BigDecimal revenueAmount;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}
