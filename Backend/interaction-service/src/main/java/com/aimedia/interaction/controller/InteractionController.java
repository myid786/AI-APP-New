package com.aimedia.interaction.controller;

import com.aimedia.interaction.dto.CreateCommentRequest;
import com.aimedia.interaction.dto.RatePostRequest;
import com.aimedia.interaction.entity.Comment;
import com.aimedia.interaction.entity.Rating;
import com.aimedia.interaction.service.InteractionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interactions")
@CrossOrigin(origins = "*")
public class InteractionController {
    private final InteractionService service;

    public InteractionController(InteractionService service) {
        this.service = service;
    }

    @PostMapping("/comments")
    public Comment comment(@Valid @RequestBody CreateCommentRequest r) {
        return service.comment(r);
    }

    @GetMapping("/comments/{postId}")
    public List<Comment> comments(@PathVariable Long postId) {
        return service.comments(postId);
    }

    @PostMapping("/ratings")
    public Rating rate(@RequestBody RatePostRequest r) {
        return service.rate(r);
    }

    @GetMapping("/ratings/{postId}/average")
    public Map<String, Double> average(@PathVariable Long postId) {
        return Map.of("average", service.average(postId));
    }
}
