package com.example.amlang.service;

import com.example.amlang.dto.CreatePostRequestDTO;
import com.example.amlang.dto.PostDetailResponseDTO;
import com.example.amlang.dto.UpdatePostRequestDTO;
import com.example.amlang.entity.Post;
import com.example.amlang.entity.PostMedia;
import com.example.amlang.entity.User;
import com.example.amlang.enums.MediaType;
import com.example.amlang.enums.Visibility;
import com.example.amlang.repository.PostRepository;
import com.example.amlang.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Transactional
    public Post createPost(CreatePostRequestDTO request) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Người dùng chưa đăng nhập");
        }

        String phoneNumber = auth.getName();
        User user = userService.findUserByPhoneNumber(phoneNumber);

        if (request.getImages() != null && request.getImages().size() > 9) {
            throw new RuntimeException("Chỉ được tải tối đa 9 ảnh");
        }

        Post post = new Post();
        post.setUser(user);
        post.setContent(request.getContent());
        post.setVisibility(request.getVisibility() != null ? Visibility.valueOf(String.valueOf(request.getVisibility())) : Visibility.PUBLIC);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        List<PostMedia> mediaList = new ArrayList<>();
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            String postUploadDir = Paths.get(uploadDir, "posts").toString();
            Files.createDirectories(Paths.get(postUploadDir));

            for (MultipartFile image : request.getImages()) {
                if (!image.isEmpty()) {
                    String fileName = "post_" + System.currentTimeMillis() + "_" + image.getOriginalFilename();
                    Path path = Paths.get(postUploadDir, fileName);
                    Files.write(path, image.getBytes());

                    PostMedia media = new PostMedia();
                    media.setPost(post);
                    media.setFileUrl("/posts/" + fileName);
                    media.setMediaType(MediaType.IMAGE);
                    media.setCreatedAt(LocalDateTime.now());
                    mediaList.add(media);
                }
            }
            post.setMedia((List<PostMedia>) mediaList);
        }

        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<PostDetailResponseDTO> getPostsByActivePhoneNumbers() {
        // Lấy tất cả bài viết từ các người dùng có bài viết
        List<Post> posts = postRepository.findAllByUsersWithPosts();

        // Chuyển đổi sang DTO
        return posts.stream().map(post -> {
            PostDetailResponseDTO dto = new PostDetailResponseDTO();
            dto.setUserName(post.getUser().getUserName());
            dto.setProfilePicture(post.getUser().getProfilePicture());
            dto.setCoverPhoto(post.getUser().getCoverPhoto());
            dto.setCreatedAt(post.getCreatedAt());
            dto.setUpdatedAt(post.getUpdatedAt());
            dto.setContent(post.getContent());
            dto.setVisibility(post.getVisibility().name());
            dto.setMediaUrls(post.getMedia() != null ? post.getMedia().stream()
                    .map(PostMedia::getFileUrl).toList() : List.of());
            dto.setUserCode(post.getUser().getUserCode());
            dto.setEdited(post.getCreatedAt().isBefore(post.getUpdatedAt()));
            return dto;
        }).collect(Collectors.toList());
    }

}