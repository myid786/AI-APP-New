package com.aimedia.interaction.repository;

import com.aimedia.interaction.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByPostIdAndUserId(Long postId, Long userId);
    List<Rating> findByPostId(Long postId);
}
