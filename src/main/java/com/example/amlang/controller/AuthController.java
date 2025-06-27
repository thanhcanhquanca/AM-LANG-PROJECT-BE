package com.example.amlang.controller;

import com.example.amlang.dto.AuthResponseDTO;
import com.example.amlang.dto.LoginRequestDTO;
import com.example.amlang.dto.RegisterRequestDTO;
import com.example.amlang.dto.UpdateProfileRequestDTO;
import com.example.amlang.entity.User;
import com.example.amlang.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            fieldError -> fieldError.getField(),
                            fieldError -> fieldError.getDefaultMessage(),
                            (existing, replacement) -> existing, HashMap::new
                    ));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            return ResponseEntity.ok(userService.login(loginRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Đăng nhập thất bại: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO registerRequest, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            fieldError -> fieldError.getField(),
                            fieldError -> fieldError.getDefaultMessage(),
                            (existing, replacement) -> existing, HashMap::new
                    ));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            AuthResponseDTO response = userService.register(registerRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Đăng ký thất bại: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String phoneNumber = authentication.getName();
            try {
                User user = userService.findUserByPhoneNumber(phoneNumber);
                if (user != null) {
                    user.setLastLogin(LocalDateTime.now());
                    userService.saveUser(user);
                    return ResponseEntity.ok(Map.of(
                            "message", "Đăng xuất thành công",
                            "userName", user.getUserName(),
                            "userId", user.getUserId(),
                            "phoneNumber", user.getPhoneNumber()
                    ));
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Đăng xuất thất bại: " + e.getMessage()));
            }
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Không tìm thấy thông tin người dùng"));
    }


    @PutMapping(value = "/update-profile", consumes = "multipart/form-data")
    public ResponseEntity<?> updateProfile(
            @Valid @ModelAttribute UpdateProfileRequestDTO request,
            BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            fieldError -> fieldError.getField(),
                            fieldError -> fieldError.getDefaultMessage(),
                            (existing, replacement) -> existing, HashMap::new
                    ));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            userService.updateProfile(request);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findUserByPhoneNumber(auth.getName());
            return ResponseEntity.ok(Map.of(
                    "message", "Cập nhật hồ sơ thành công",
                    "userName", user.getUserName(),
                    "email", user.getEmail(),
                    "bio", user.getBio(),
                    "userCode", user.getUserCode(), // Thêm userCode
                    "profilePicture", user.getProfilePicture(),
                    "coverPhoto", user.getCoverPhoto()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Cập nhật thất bại: " + e.getMessage()));
        }
    }

}