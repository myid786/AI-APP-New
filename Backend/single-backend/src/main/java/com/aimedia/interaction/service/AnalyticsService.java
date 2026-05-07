package com.aimedia.interaction.service;

import com.aimedia.interaction.dto.PostAnalyticsResponse;
import com.aimedia.interaction.entity.Comment;
import com.aimedia.interaction.repository.CommentRepository;
import com.aimedia.interaction.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsService {
    private final CommentRepository comments;
    private final RatingRepository ratings;

    public AnalyticsService(CommentRepository comments, RatingRepository ratings) {
        this.comments = comments;
        this.ratings = ratings;
    }

    public PostAnalyticsResponse analytics(Long postId) {
        List<Comment> list = comments.findByPostId(postId);
        double avg = ratings.findByPostId(postId).stream().mapToInt(r -> r.getRating()).average().orElse(0.0);
        long pos = list.stream().filter(c -> "POSITIVE".equals(c.getSentiment())).count();
        long neu = list.stream().filter(c -> "NEUTRAL".equals(c.getSentiment())).count();
        long neg = list.stream().filter(c -> "NEGATIVE".equals(c.getSentiment())).count();
        return new PostAnalyticsResponse(postId, list.size(), avg, pos, neu, neg);
    }
}
