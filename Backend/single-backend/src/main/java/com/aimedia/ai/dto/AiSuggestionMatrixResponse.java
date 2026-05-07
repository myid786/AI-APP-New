package com.aimedia.ai.dto;

import java.util.List;
import java.util.Map;

public record AiSuggestionMatrixResponse(
        int overallScore,
        String verdict,
        List<AiSuggestionMatrixRow> matrix,
        List<String> nextActions,
        String aiProvider,
        String azureSentiment,
        Map<String, Double> azureConfidenceScores
) {}
