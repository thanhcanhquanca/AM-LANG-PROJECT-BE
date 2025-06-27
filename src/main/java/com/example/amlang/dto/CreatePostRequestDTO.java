package com.example.amlang.dto;


import com.example.amlang.enums.Visibility;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequestDTO {
    @Size(max = 1000, message = "Nội dung không được vượt quá 1000 ký tự")
    private String content;

    private List<MultipartFile> images; // Danh sách ảnh (tối đa 9)

    @Enumerated(EnumType.STRING)
    private Visibility visibility; // Có thể là PUBLIC, PRIVATE, ONLY_ME
}
