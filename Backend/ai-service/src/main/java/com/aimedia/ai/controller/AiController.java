package com.aimedia.ai.controller;

import com.aimedia.ai.dto.AiCaptionRequest;
import com.aimedia.ai.dto.AiCaptionResponse;
import com.aimedia.ai.service.AiHelperService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {
    private final AiHelperService service;

    public AiController(AiHelperService service) {
        this.service = service;
    }

    @PostMapping("/caption")
    public AiCaptionResponse caption(@RequestBody AiCaptionRequest request) {
        return service.caption(request);
    }
}
