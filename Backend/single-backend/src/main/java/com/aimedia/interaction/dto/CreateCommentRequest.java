package com.aimedia.interaction.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequest(Long postId, Long userId, String username, @NotBlank String text) {}
