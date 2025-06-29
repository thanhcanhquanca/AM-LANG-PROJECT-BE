package com.example.amlang.entity;

import com.example.amlang.enums.Visibility;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "playlists", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_visibility", columnList = "visibility")
})
@Data
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playlistId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Size(max = 100)
    @Column(nullable = false)
    private String playlistName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Size(max = 255)
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('PUBLIC', 'PRIVATE', 'ONLY_ME') DEFAULT 'PUBLIC'")
    private Visibility visibility = Visibility.PUBLIC;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "playlist", fetch = FetchType.LAZY)
    private List<PlaylistVideo> videos;
}