package com.example.amlang.controller;

import com.example.amlang.dto.CreatePostRequestDTO;
import com.example.amlang.dto.PostDetailResponseDTO;
import com.example.amlang.dto.UpdatePostRequestDTO;
import com.example.amlang.entity.Post;
import com.example.amlang.entity.PostMedia;
import com.example.amlang.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin("*")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(@Valid @ModelAttribute CreatePostRequestDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == "anonymousUser") {
            return ResponseEntity.status(401).body(Map.of("error", "Vui lòng đăng nhập để đăng bài"));
        }

        try {
            Post post = postService.createPost(request);

            PostDetailResponseDTO response = new PostDetailResponseDTO();
            response.setUserName(post.getUser().getUserName());
            response.setProfilePicture(post.getUser().getProfilePicture());
            response.setCoverPhoto(post.getUser().getCoverPhoto());
            response.setCreatedAt(post.getCreatedAt());
            response.setUpdatedAt(post.getUpdatedAt());
            response.setContent(post.getContent());
            response.setVisibility(post.getVisibility().name());
            response.setMediaUrls(post.getMedia() != null ? post.getMedia().stream()
                    .map(PostMedia::getFileUrl).toList() : List.of());
            response.setUserCode(post.getUser().getUserCode());
            response.setEdited(post.getCreatedAt().isBefore(post.getUpdatedAt()));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Đăng bài thất bại: " + e.getMessage()));
        }
    }

}