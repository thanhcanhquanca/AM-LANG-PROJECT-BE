package com.example.amlang.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{3,16}$", message = "Số điện thoại phải từ 3 đến 16 chữ số")
    private String phoneNumber;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}
