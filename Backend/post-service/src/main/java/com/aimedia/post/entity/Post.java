package com.aimedia.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long creatorId;
    private String creatorUsername;
    private String title;

    @Column(length = 2000)
    private String caption;

    private String location;
    private String peopleTags;
    private String mediaUrl;
    private String mediaType;
    private LocalDateTime createdAt;
}
