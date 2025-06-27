package com.example.amlang.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "comment_mentions", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id")
})
@Data
public class CommentMention {
    @Id
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
