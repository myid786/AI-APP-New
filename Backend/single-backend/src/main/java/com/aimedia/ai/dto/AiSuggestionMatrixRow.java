package com.aimedia.ai.dto;

public record AiSuggestionMatrixRow(
        String metric,
        int score,
        String status,
        String suggestion,
        String priority
) {}
