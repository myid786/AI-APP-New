package com.aimedia.post.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePostRequest(Long creatorId, String creatorUsername, @NotBlank String title, String caption, String location, String peopleTags, @NotBlank String mediaUrl, @NotBlank String mediaType) {}
