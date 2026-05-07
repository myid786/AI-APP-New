package com.aimedia.ai.service;

import java.util.Map;

public record AzureLanguageInsight(
        String sentiment,
        Map<String, Double> confidenceScores
) {}
