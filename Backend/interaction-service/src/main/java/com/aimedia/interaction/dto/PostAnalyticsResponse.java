package com.aimedia.interaction.dto;

public record PostAnalyticsResponse(Long postId, long totalComments, double averageRating, long positiveComments, long neutralComments, long negativeComments) {}
