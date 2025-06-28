package com.example.amlang.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDetailResponseDTO {
    private String userName;
    private String profilePicture;
    private String coverPhoto;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt; // Thêm thời gian chỉnh sửa
    private String content;
    private List<String> mediaUrls;
    private String visibility;
    private String userCode;
    private boolean isEdited; // Cờ chỉ ra bài viết đã được chỉnh sửa
}