package com.aimedia.post.service;

import com.aimedia.post.dto.CreatePostRequest;
import com.aimedia.post.entity.Post;
import com.aimedia.post.repository.PostRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class PostService {
    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }


    public Post create(CreatePostRequest r) {
        Post p = new Post();
        p.setCreatorId(r.creatorId());
        p.setCreatorUsername(r.creatorUsername());
        p.setTitle(r.title());
        p.setCaption(r.caption());
        p.setLocation(r.location());
        p.setPeopleTags(r.peopleTags());
        p.setMediaUrl(r.mediaUrl());
        p.setMediaType(r.mediaType());
        p.setCreatedAt(LocalDateTime.now());
        return repository.save(p);
    }


    public List<Post> all() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .toList();
    }

    public List<Post> search(String q) {
        return repository.findByTitleContainingIgnoreCaseOrCaptionContainingIgnoreCaseOrLocationContainingIgnoreCase(q, q, q);
    }

    public List<Post> byCreator(Long id) {
        return repository.findByCreatorId(id);
    }

}
