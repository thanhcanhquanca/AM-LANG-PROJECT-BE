package com.example.amlang.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "Tên người dùng không được để trống")
    @Size(min = 3, max = 50, message = "Tên người dùng phải từ 3 đến 50 ký tự")
    @Column(nullable = false)
    private String userName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{4,20}$", message = "Số điện thoại phải từ 4 đến 20 chữ số")
    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Email(message = "Email không hợp lệ")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "Vai trò không được để trống")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @NotNull(message = "Trạng thái không được để trống")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime lastLogin;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private LocalDateTime dateOfBirth;

    private String profilePicture;

    private String coverPhoto;

    @Column(unique = true)
    private String userCode;

    @Column(name = "two_fa_secret")
    private String twoFaSecret;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int copyrightViolations;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int warningCount;

    @Column(length = 500)
    private String qrCode;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Video> videos;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Follow> followers;

    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Follow> following;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum UserStatus {
        ACTIVE, INACTIVE, LOCKED, SUSPENDED, DELETED
    }
}
