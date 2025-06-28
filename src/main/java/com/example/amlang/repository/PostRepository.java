package com.example.amlang.repository;


import com.example.amlang.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser_PhoneNumber(String phoneNumber);
}
