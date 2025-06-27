package com.example.amlang.entity;

import com.example.amlang.enums.CopyrightStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "video_copyrights")
@Data
public class VideoCopyright {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long copyrightId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isAiGenerated;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('VERIFIED', 'DISPUTED', 'VIOLATED', 'REMOVED') DEFAULT 'VERIFIED'")
    private CopyrightStatus status = CopyrightStatus.VERIFIED;

    @Column(columnDefinition = "TEXT")
    private String claimDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claimant_id")
    private User claimant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private Report report;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}
