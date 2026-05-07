package com.aimedia.ai.service;

import com.aimedia.ai.dto.AiCaptionRequest;
import com.aimedia.ai.dto.AiCaptionResponse;
import com.aimedia.ai.dto.AiContentPlanRequest;
import com.aimedia.ai.dto.AiContentPlanResponse;
import com.aimedia.ai.dto.AiSuggestionMatrixRequest;
import com.aimedia.ai.dto.AiSuggestionMatrixResponse;
import com.aimedia.ai.dto.AiSuggestionMatrixRow;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class AiHelperService {
    public AiCaptionResponse caption(AiCaptionRequest r) {
        AiContentPlanResponse plan = contentPlan(new AiContentPlanRequest(
                r.title(),
                r.location(),
                r.peopleTags(),
                null,
                null));
        return new AiCaptionResponse(plan.caption(), plan.hashtags());
    }

    public AiContentPlanResponse contentPlan(AiContentPlanRequest r) {
        String title = valueOrDefault(r.title(), "new media moment");
        String location = valueOrDefault(r.location(), "your city");
        String mediaType = valueOrDefault(r.mediaType(), "post").toLowerCase(Locale.ROOT);
        String mood = valueOrDefault(r.mood(), "fresh");
        String people = valueOrDefault(r.peopleTags(), "the community");

        List<String> hashtags = buildHashtags(location, mediaType, mood);
        String caption = "A " + mood + " look at " + title + " in " + location
                + " with " + people + ". What part would you replay?";
        String altText = "A " + mediaType + " post titled " + title + " captured in " + location + ".";
        String hook = mediaType.contains("video")
                ? "Open with the most energetic 2 seconds and keep the first caption line short."
                : "Lead with the strongest visual detail and one clear emotion.";
        String bestTime = location.toLowerCase(Locale.ROOT).contains("uk")
                ? "Post between 6 PM and 9 PM UK time for stronger evening engagement."
                : "Post between 6 PM and 9 PM local time for stronger evening engagement.";

        return new AiContentPlanResponse(
                caption,
                hashtags,
                altText,
                hook,
                bestTime,
                "Looks safe for a general audience. Review private faces, personal data, and copyrighted music before publishing."
        );
    }

    public AiSuggestionMatrixResponse suggestionMatrix(AiSuggestionMatrixRequest r) {
        String title = valueOrDefault(r.title(), "");
        String caption = valueOrDefault(r.caption(), "");
        String location = valueOrDefault(r.location(), "");
        String peopleTags = valueOrDefault(r.peopleTags(), "");
        String mediaType = valueOrDefault(r.mediaType(), "post").toLowerCase(Locale.ROOT);
        String mood = valueOrDefault(r.mood(), "fresh");

        List<AiSuggestionMatrixRow> rows = new ArrayList<>();
        rows.add(row(
                "Title strength",
                scoreTitle(title),
                "Use a clear title with a place, moment, or promise.",
                "High"));
        rows.add(row(
                "Caption clarity",
                scoreCaption(caption),
                "Keep caption between 80 and 180 characters with one question or call to action.",
                "High"));
        rows.add(row(
                "Discovery tags",
                scoreTags(caption, mood),
                "Add 4 to 7 relevant hashtags, mixing brand, media type, mood, and city.",
                "Medium"));
        rows.add(row(
                "Audience context",
                scoreContext(location, peopleTags),
                "Add location and people/community tags so search and recommendations have stronger context.",
                "Medium"));
        rows.add(row(
                "Media readiness",
                scoreMediaReadiness(mediaType, caption),
                mediaType.contains("video")
                        ? "For video, lead with the strongest two seconds and mention the key action in the caption."
                        : "For image, mention the strongest visual detail and the emotion it should create.",
                "High"));
        rows.add(row(
                "Safety review",
                scoreSafety(caption, peopleTags),
                "Review faces, private data, copyrighted music, location sensitivity, and risky wording before publishing.",
                "High"));

        int overall = rows.stream().mapToInt(AiSuggestionMatrixRow::score).sum() / rows.size();
        List<String> actions = rows.stream()
                .filter(row -> row.score() < 80)
                .sorted((a, b) -> Integer.compare(priorityRank(b.priority()), priorityRank(a.priority())))
                .limit(3)
                .map(row -> row.metric() + ": " + row.suggestion())
                .toList();

        return new AiSuggestionMatrixResponse(
                overall,
                verdict(overall),
                rows,
                actions.isEmpty() ? List.of("Ready to publish. Do one final media preview check.") : actions);
    }

    private String valueOrDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }

    private List<String> buildHashtags(String location, String mediaType, String mood) {
        List<String> tags = new ArrayList<>(List.of("#AIMedia", "#creator", "#media"));
        tags.add("#" + mediaType.replaceAll("[^A-Za-z0-9]", ""));
        tags.add("#" + mood.replaceAll("[^A-Za-z0-9]", ""));
        String city = location.split(",")[0].replaceAll("[^A-Za-z0-9]", "");
        if (!city.isBlank()) {
            tags.add("#" + city);
        }
        return tags;
    }

    private AiSuggestionMatrixRow row(String metric, int score, String suggestion, String priority) {
        return new AiSuggestionMatrixRow(metric, score, status(score), suggestion, priority);
    }

    private int scoreTitle(String title) {
        if (title.isBlank()) return 35;
        int score = 55;
        if (title.length() >= 12) score += 15;
        if (title.length() <= 70) score += 10;
        if (title.matches(".*[A-Za-z].*")) score += 10;
        if (title.matches(".*\\b(in|at|from|with|near)\\b.*")) score += 10;
        return clamp(score);
    }

    private int scoreCaption(String caption) {
        if (caption.isBlank()) return 30;
        int score = 45;
        int length = caption.length();
        if (length >= 80 && length <= 220) score += 25;
        else if (length >= 40) score += 15;
        if (caption.contains("?")) score += 10;
        if (caption.matches(".*\\b(watch|share|comment|replay|follow|save|tell)\\b.*")) score += 10;
        if (caption.split("\\s+").length >= 8) score += 10;
        return clamp(score);
    }

    private int scoreTags(String caption, String mood) {
        long hashtagCount = caption.chars().filter(ch -> ch == '#').count();
        int score = hashtagCount >= 4 && hashtagCount <= 7 ? 85 : hashtagCount > 0 ? 65 : 40;
        if (!mood.isBlank() && caption.toLowerCase(Locale.ROOT).contains(mood.toLowerCase(Locale.ROOT))) {
            score += 10;
        }
        return clamp(score);
    }

    private int scoreContext(String location, String peopleTags) {
        int score = 35;
        if (!location.isBlank()) score += 30;
        if (location.contains(",")) score += 10;
        if (!peopleTags.isBlank()) score += 20;
        if (peopleTags.split(",").length >= 2) score += 5;
        return clamp(score);
    }

    private int scoreMediaReadiness(String mediaType, String caption) {
        int score = 60;
        if (mediaType.contains("video")) {
            if (caption.toLowerCase(Locale.ROOT).matches(".*\\b(replay|watch|seconds|moment|clip)\\b.*")) score += 25;
        } else {
            if (caption.toLowerCase(Locale.ROOT).matches(".*\\b(look|visual|scene|view|detail)\\b.*")) score += 25;
        }
        if (!caption.isBlank()) score += 10;
        return clamp(score);
    }

    private int scoreSafety(String caption, String peopleTags) {
        String combined = (caption + " " + peopleTags).toLowerCase(Locale.ROOT);
        int score = 90;
        if (combined.matches(".*\\b(phone|email|address|password|license|passport)\\b.*")) score -= 30;
        if (combined.matches(".*\\b(kid|child|school|hospital|private)\\b.*")) score -= 20;
        if (combined.matches(".*\\b(copyright|song|music|brand)\\b.*")) score -= 10;
        return clamp(score);
    }

    private String status(int score) {
        if (score >= 85) return "Strong";
        if (score >= 70) return "Good";
        if (score >= 50) return "Improve";
        return "Needs work";
    }

    private String verdict(int score) {
        if (score >= 85) return "Ready to publish";
        if (score >= 70) return "Good, improve the weak rows first";
        if (score >= 50) return "Needs edits before publishing";
        return "Not ready yet";
    }

    private int clamp(int score) {
        return Math.max(0, Math.min(100, score));
    }

    private int priorityRank(String priority) {
        return switch (priority) {
            case "High" -> 3;
            case "Medium" -> 2;
            default -> 1;
        };
    }
}
