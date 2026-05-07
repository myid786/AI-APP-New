package com.aimedia.ai.dto;

import java.util.List;

public record AiSuggestionMatrixResponse(
        int overallScore,
        String verdict,
        List<AiSuggestionMatrixRow> matrix,
        List<String> nextActions
) {}
