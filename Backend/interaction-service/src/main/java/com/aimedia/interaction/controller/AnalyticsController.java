package com.aimedia.interaction.controller;

import com.aimedia.interaction.dto.PostAnalyticsResponse;
import com.aimedia.interaction.service.AnalyticsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interactions/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {
    private final AnalyticsService service;

    public AnalyticsController(AnalyticsService service) {
        this.service = service;
    }

    @GetMapping("/{postId}")
    public PostAnalyticsResponse analytics(@PathVariable Long postId) {
        return service.analytics(postId);
    }
}
