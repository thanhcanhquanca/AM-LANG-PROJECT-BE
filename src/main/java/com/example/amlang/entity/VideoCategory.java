package com.example.amlang.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "video_categories")
@Data
public class VideoCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @NotNull
    @Size(max = 100)
    @Column(nullable = false, unique = true)
    private String categoryName;

    @Size(max = 255)
    private String iconUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "videoCategory", fetch = FetchType.LAZY)
    private List<Video> videos;
}