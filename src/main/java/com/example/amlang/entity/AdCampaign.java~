package com.example.amlang.entity;


import com.example.amlang.enums.CampaignObjective;
import com.example.amlang.enums.CampaignStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "ad_campaigns")
@Data
public class AdCampaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campaignId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @NotNull
    @Size(max = 255)
    @Column(nullable = false)
    private String campaignName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CampaignObjective objective;

    @NotNull
    @Column(columnDefinition = "DECIMAL(15,2) DEFAULT 0.00", nullable = false)
    private BigDecimal budget;

    @NotNull
    @Column(columnDefinition = "DECIMAL(15,2) DEFAULT 0.00", nullable = false)
    private BigDecimal spentAmount;

    @NotNull
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime startDate;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('DRAFT', 'ACTIVE', 'PAUSED', 'COMPLETED', 'CANCELLED') DEFAULT 'DRAFT'")
    private CampaignStatus status = CampaignStatus.DRAFT;

    @Column(columnDefinition = "TEXT")
    private String targetAudience;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY)
    private Set<Ad> ads;
}
