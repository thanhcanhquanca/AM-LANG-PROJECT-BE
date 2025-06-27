package com.example.amlang.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "comments", indexes = {
        @Index(name = "idx_post_id", columnList = "post_id"),
        @Index(name = "idx_video_id", columnList = "video_id"),
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_parent_comment_id", columnList = "parent_comment_id")
})
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    private Set<CommentImage> images;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    private Set<CommentLike> likes;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    private Set<CommentMention> mentions;
}