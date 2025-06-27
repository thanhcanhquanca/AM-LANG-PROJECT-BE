package com.example.amlang.service;

import com.example.amlang.config.JwtUtil;
import com.example.amlang.dto.AuthResponseDTO;
import com.example.amlang.dto.LoginRequestDTO;
import com.example.amlang.dto.RegisterRequestDTO;
import com.example.amlang.dto.UpdateProfileRequestDTO;
import com.example.amlang.entity.Permission;
import com.example.amlang.entity.User;
import com.example.amlang.repository.PermissionRepository;
import com.example.amlang.repository.RoleRepository;
import com.example.amlang.repository.UserRepository;
import com.example.amlang.security.CustomUserDetailsService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Value("${file.upload-dir}")
    private String uploadDir;



    @Transactional(readOnly = true)
    public AuthResponseDTO login(LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getPhoneNumber(),
                            loginRequest.getPassword()
                    )
            );
            System.out.println("Authentication successful for: " + loginRequest.getPhoneNumber());

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);
            User user = userRepository.findByPhoneNumber(loginRequest.getPhoneNumber())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<String> permissions = permissionRepository.findByRoleRoleIdAndIsAllowedTrue(user.getRole().getRoleId())
                    .stream()
                    .map(Permission::getPermissionName)
                    .collect(Collectors.toList());

            AuthResponseDTO response = new AuthResponseDTO();
            response.setToken(token);
            response.setUserName(user.getUserName());
            response.setRole(user.getRole().getRoleName());
            response.setPermissions(permissions);
            response.setProfilePicture(user.getProfilePicture());
            response.setUserCode(user.getUserCode());
            response.setQrCode(user.getQrCode());
            return response;
        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());
            throw new RuntimeException("Đăng nhập thất bại: " + e.getMessage());
        }
    }

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO registerRequest) {
        if (userRepository.findByPhoneNumber(registerRequest.getPhoneNumber()).isPresent()) {
            throw new RuntimeException("Số điện thoại đã được sử dụng");
        }

        String userCode = generateUniqueUserCode();
        while (userRepository.findByUserCode(userCode).isPresent()) {
            userCode = generateUniqueUserCode();
        }

        String baseUploadDir = Paths.get(uploadDir, "uploads").toString();
        String qrCodePath = generateQRCodeFile(registerRequest.getPhoneNumber(), baseUploadDir);

        // Tạo userName ngẫu nhiên 6 ký tự và đảm bảo không trùng
        String userName = generateUniqueUserName();

        User user = new User();
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setUserName(userName);
        user.setRole(roleRepository.findByRoleName("USER").orElseThrow(() -> new RuntimeException("Role not found")));
        user.setUserCode(userCode);
        user.setQrCode(qrCodePath);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(null);
        response.setUserName(savedUser.getUserName());
        response.setRole(savedUser.getRole().getRoleName());
        response.setPermissions(permissionRepository.findByRoleRoleIdAndIsAllowedTrue(savedUser.getRole().getRoleId())
                .stream()
                .map(Permission::getPermissionName)
                .collect(Collectors.toList()));
        response.setProfilePicture(savedUser.getProfilePicture());
        response.setUserCode(savedUser.getUserCode());
        response.setQrCode(savedUser.getQrCode());
        return response;
    }

    @Transactional
    public void updateProfile(UpdateProfileRequestDTO request) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Người dùng chưa đăng nhập");
        }

        String phoneNumber = auth.getName();
        User user = findUserByPhoneNumber(phoneNumber);

        // Kiểm tra email duy nhất (trừ email của chính user hiện tại)
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            User existingUserWithEmail = userRepository.findByEmail(request.getEmail())
                    .filter(u -> !u.getPhoneNumber().equals(phoneNumber)) // Loại trừ user hiện tại
                    .orElse(null);
            if (existingUserWithEmail != null) {
                throw new RuntimeException("Email đã được sử dụng bởi người khác");
            }
        }

        // Cập nhật thông tin văn bản
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setBio(request.getBio());

        // Xử lý ảnh cá nhân
        String newProfilePicturePath = null;
        if (request.getProfilePicture() != null && !request.getProfilePicture().isEmpty()) {
            newProfilePicturePath = saveFile(request.getProfilePicture(), "profile", "profile-photos");
            if (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
                deleteFile(user.getProfilePicture()); // Xóa ảnh cũ
            }
            user.setProfilePicture(newProfilePicturePath);
        }

        // Xử lý ảnh bìa
        String newCoverPhotoPath = null;
        if (request.getCoverPhoto() != null && !request.getCoverPhoto().isEmpty()) {
            newCoverPhotoPath = saveFile(request.getCoverPhoto(), "cover", "profile-photos");
            if (user.getCoverPhoto() != null && !user.getCoverPhoto().isEmpty()) {
                deleteFile(user.getCoverPhoto()); // Xóa ảnh cũ
            }
            user.setCoverPhoto(newCoverPhotoPath);
        }

        saveUser(user);
    }

    private String saveFile(MultipartFile file, String type, String subDir) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String uploadDirPath = Paths.get(uploadDir, subDir).toString(); // Sử dụng uploads hoặc profile-photos
        Files.createDirectories(Paths.get(uploadDirPath));
        String fileName = type + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDirPath, fileName);
        Files.write(path, file.getBytes());
        return "/" + subDir + "/" + fileName; // Trả về đường dẫn với tiền tố /uploads/ hoặc /profile-photos/
    }

    private void deleteFile(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            String basePath = uploadDir;
            String subPath = filePath.substring(1); // Loại bỏ dấu / đầu tiên
            Path path = Paths.get(basePath, subPath);
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                System.err.println("Lỗi khi xóa tệp: " + e.getMessage());
            }
        }
    }


    @Transactional
    public void saveUser(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    public User findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found with phone number: " + phoneNumber));
    }

    private String generateUniqueUserCode() {
        SecureRandom random = new SecureRandom();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(9);
        for (int i = 0; i < 9; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return "@" + sb.toString();
    }

    private String generateUniqueUserName() {
        SecureRandom random = new SecureRandom();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb;
        String userName;

        do {
            sb = new StringBuilder(6);
            for (int i = 0; i < 6; i++) {
                sb.append(characters.charAt(random.nextInt(characters.length())));
            }
            userName = sb.toString();
        } while (userRepository.findByUserName(userName).isPresent());

        return userName;
    }

    private String generateQRCodeFile(String phoneNumber, String baseDir) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(phoneNumber, BarcodeFormat.QR_CODE, 200, 200);

            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            byte[] bytes = baos.toByteArray();

            String fileName = "qr_" + phoneNumber + "_" + System.currentTimeMillis() + ".png";
            Path path = Paths.get(baseDir, fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, bytes);

            return "/uploads/" + fileName;
        } catch (WriterException | IOException e) {
            System.err.println("Error generating QR code: " + e.getMessage());
            throw new RuntimeException("Failed to generate QR code: " + e.getMessage());
        }
    }
}