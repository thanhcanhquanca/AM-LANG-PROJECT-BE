package com.example.amlang.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "video_views", indexes = {
        @Index(name = "idx_video_id_user_id", columnList = "video_id, user_id")
})
@Data
public class VideoView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime viewedAt;
}
