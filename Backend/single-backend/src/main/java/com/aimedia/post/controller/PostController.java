package com.aimedia.post.controller;

import com.aimedia.post.dto.CreatePostRequest;
import com.aimedia.post.entity.Post;
import com.aimedia.post.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @PostMapping
    public Post create(@Valid @RequestBody CreatePostRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<Post> all() {
        return service.all();
    }

    @GetMapping("/search")
    public List<Post> search(@RequestParam String q) {
        return service.search(q);
    }

    @GetMapping("/creator/{creatorId}")
    public List<Post> byCreator(@PathVariable Long creatorId) {
        return service.byCreator(creatorId);
    }



}
