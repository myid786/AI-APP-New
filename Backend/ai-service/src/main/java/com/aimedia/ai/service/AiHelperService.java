package com.aimedia.ai.service;

import com.aimedia.ai.dto.AiCaptionRequest;
import com.aimedia.ai.dto.AiCaptionResponse;
import com.aimedia.ai.dto.AiContentPlanRequest;
import com.aimedia.ai.dto.AiContentPlanResponse;
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
}
