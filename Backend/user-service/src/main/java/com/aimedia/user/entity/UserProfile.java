package com.aimedia.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private Long userId;

    @Column(nullable=false)
    private String username;

    private String fullName;
    private String bio;
    private String profileImageUrl;
    private String role;
    private String location;
    private String website;
}
