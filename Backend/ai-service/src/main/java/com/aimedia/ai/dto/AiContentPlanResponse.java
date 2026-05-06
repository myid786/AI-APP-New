package com.aimedia.ai.dto;

import java.util.List;

public record AiContentPlanResponse(
        String caption,
        List<String> hashtags,
        String altText,
        String hook,
        String bestTimeToPost,
        String contentSafetyNote
) {}
