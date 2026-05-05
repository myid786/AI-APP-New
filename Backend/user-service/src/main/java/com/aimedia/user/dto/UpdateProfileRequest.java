package com.aimedia.user.dto;

public record UpdateProfileRequest(String fullName, String bio, String profileImageUrl, String location, String website) {}
