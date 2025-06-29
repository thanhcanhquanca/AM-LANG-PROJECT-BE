package com.example.amlang.repository;


import com.example.amlang.entity.Post;
import com.example.amlang.entity.User;
import com.example.amlang.enums.Visibility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByVisibility(Visibility visibility);
    Page<Post> findAll(Pageable pageable);
    List<Post> findByUser(User user);

    @Query("SELECT p FROM Post p JOIN p.user u WHERE u.phoneNumber IN (SELECT u2.phoneNumber FROM User u2 JOIN u2.posts p2 WHERE p2 IS NOT NULL) AND p.visibility = 'PUBLIC'")
    List<Post> findAllByUsersWithPosts();

}
