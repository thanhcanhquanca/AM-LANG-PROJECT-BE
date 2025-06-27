package com.example.amlang.entity;

import com.example.amlang.enums.Visibility;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "videos", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_video_category_id", columnList = "video_category_id"),
        @Index(name = "idx_visibility", columnList = "visibility")
})
@Data
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_category_id", nullable = false)
    private VideoCategory videoCategory;

    @NotNull
    @Size(max = 255)
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Size(max = 255)
    @Column(nullable = false)
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('PUBLIC', 'PRIVATE', 'ONLY_ME') DEFAULT 'PUBLIC'")
    private Visibility visibility = Visibility.PUBLIC;

    @NotNull
    @Column(columnDefinition = "INT DEFAULT 0", nullable = false)
    private Integer totalDuration;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<VideoMedia> media;

    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<VideoView> views;

    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<VideoLike> likes;

    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<VideoShare> shares;

    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PlaylistVideo> playlists;

    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<WatchLater> watchLater;

    @OneToOne(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private VideoMonetization monetization;

    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<VideoCopyright> copyrights;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
