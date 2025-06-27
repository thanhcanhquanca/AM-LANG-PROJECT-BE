package com.example.amlang.entity;

import com.example.amlang.enums.MediaType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_media", indexes = {
        @Index(name = "idx_post_id", columnList = "post_id")
})
@Data
public class PostMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @NotNull
    @Size(max = 255)
    @Column(nullable = false)
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('IMAGE', 'VIDEO') DEFAULT 'IMAGE'")
    private MediaType mediaType = MediaType.IMAGE;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
