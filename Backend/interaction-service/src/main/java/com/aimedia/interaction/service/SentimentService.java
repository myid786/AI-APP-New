package com.aimedia.interaction.service;

import org.springframework.stereotype.Service;

@Service
public class SentimentService {
    public String analyze(String text) {
        String lower = text == null ? "" : text.toLowerCase();
        if (lower.contains("good") || lower.contains("great") || lower.contains("love")) return "POSITIVE";
        if (lower.contains("bad") || lower.contains("hate") || lower.contains("worst")) return "NEGATIVE";
        return "NEUTRAL";
    }
}
