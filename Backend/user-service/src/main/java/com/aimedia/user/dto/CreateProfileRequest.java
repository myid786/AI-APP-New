package com.aimedia.user.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateProfileRequest(Long userId, @NotBlank String username, String fullName, String bio, String profileImageUrl, String role, String location, String website) {}
