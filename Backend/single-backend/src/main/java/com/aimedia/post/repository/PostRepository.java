package com.aimedia.post.repository;

import com.aimedia.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCreatorId(Long creatorId);
    List<Post> findByTitleContainingIgnoreCaseOrCaptionContainingIgnoreCaseOrLocationContainingIgnoreCase(String title, String caption, String location);
}
