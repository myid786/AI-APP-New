package com.aimedia.media.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "media_assets")
@Getter
@Setter
public class MediaAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String blobName;

    @Column(nullable = false, length = 1000)
    private String url;

    @Column(nullable = false)
    private String type;

    private String originalFilename;
    private String contentType;
    private Long sizeBytes;
    private LocalDateTime createdAt;
}
