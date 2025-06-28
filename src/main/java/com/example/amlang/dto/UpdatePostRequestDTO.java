package com.example.amlang.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostRequestDTO {
    @Size(max = 1000, message = "Nội dung không được vượt quá 1000 ký tự")
    private String content;

    private String visibility; // "PUBLIC", "PRIVATE", hoặc "ONLY_ME"
}