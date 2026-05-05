package com.aimedia.ai.service;

import com.aimedia.ai.dto.AiCaptionRequest;
import com.aimedia.ai.dto.AiCaptionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiHelperService {
    public AiCaptionResponse caption(AiCaptionRequest r) {
        String loc = r.location() == null || r.location().isBlank() ? "the city" : r.location();
        String title = r.title() == null || r.title().isBlank() ? "new memory" : r.title();
        return new AiCaptionResponse(
                "Capturing " + title + " at " + loc + " with amazing vibes.",
                List.of("#creator", "#media", "#travel", "#viral")
        );
    }
}
