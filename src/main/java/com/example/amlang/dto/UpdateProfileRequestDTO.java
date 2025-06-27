package com.example.amlang.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequestDTO {

    @NotBlank(message = "Tên không được để trống")
    @Size(min = 3, max = 50, message = "Tên phải từ 3 đến 50 ký tự")
    private String userName;

    @Email(message = "Email không hợp lệ")
    private String email;

    @Size(max = 500, message = "Bio không được vượt quá 500 ký tự")
    private String bio;

    private MultipartFile profilePicture; // Ảnh cá nhân mới

    private MultipartFile coverPhoto;     // Ảnh bìa mới
}