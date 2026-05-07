package com.aimedia.ai.dto;

public record AiSuggestionMatrixRequest(
        String title,
        String caption,
        String location,
        String peopleTags,
        String mediaType,
        String mood
) {}
