package com.aimedia.interaction.service;

import com.aimedia.interaction.dto.CreateCommentRequest;
import com.aimedia.interaction.dto.RatePostRequest;
import com.aimedia.interaction.entity.Comment;
import com.aimedia.interaction.entity.Rating;
import com.aimedia.interaction.repository.CommentRepository;
import com.aimedia.interaction.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InteractionService {
    private final CommentRepository comments;
    private final RatingRepository ratings;
    private final SentimentService sentiment;

    public InteractionService(CommentRepository comments, RatingRepository ratings, SentimentService sentiment) {
        this.comments = comments;
        this.ratings = ratings;
        this.sentiment = sentiment;
    }

    public Comment comment(CreateCommentRequest r) {
        Comment c = new Comment();
        c.setPostId(r.postId());
        c.setUserId(r.userId());
        c.setUsername(r.username());
        c.setText(r.text());
        c.setSentiment(sentiment.analyze(r.text()));
        c.setCreatedAt(LocalDateTime.now());
        return comments.save(c);
    }

    public List<Comment> comments(Long postId) {
        return comments.findByPostId(postId);
    }

    public Rating rate(RatePostRequest r) {
        Rating rating = ratings.findByPostIdAndUserId(r.postId(), r.userId()).orElseGet(Rating::new);
        rating.setPostId(r.postId());
        rating.setUserId(r.userId());
        rating.setRating(r.rating());
        return ratings.save(rating);
    }

    public double average(Long postId) {
        return ratings.findByPostId(postId).stream().mapToInt(Rating::getRating).average().orElse(0.0);
    }
}
