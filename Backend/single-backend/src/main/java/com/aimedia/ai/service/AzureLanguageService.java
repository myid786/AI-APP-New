package com.aimedia.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@Service
public class AzureLanguageService {
    private final String endpoint;
    private final String key;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public AzureLanguageService(
            @Value("${app.azure-ai.language.endpoint:}") String endpoint,
            @Value("${app.azure-ai.language.key:}") String key,
            ObjectMapper objectMapper) {
        this.endpoint = endpoint == null ? "" : endpoint.replaceAll("/$", "");
        this.key = key == null ? "" : key;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public boolean isConfigured() {
        return StringUtils.hasText(endpoint) && StringUtils.hasText(key);
    }

    public Optional<AzureLanguageInsight> analyzeSentiment(String text) {
        if (!isConfigured() || !StringUtils.hasText(text)) {
            return Optional.empty();
        }

        try {
            Map<String, Object> body = Map.of(
                    "kind", "SentimentAnalysis",
                    "parameters", Map.of("modelVersion", "latest"),
                    "analysisInput", Map.of(
                            "documents", new Object[]{
                                    Map.of("id", "1", "language", "en", "text", text)
                            }
                    )
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint + "/language/:analyze-text?api-version=2023-04-01"))
                    .timeout(Duration.ofSeconds(10))
                    .header("Ocp-Apim-Subscription-Key", key)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body)))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                return Optional.empty();
            }

            JsonNode document = objectMapper.readTree(response.body())
                    .path("results")
                    .path("documents")
                    .path(0);
            if (document.isMissingNode()) {
                return Optional.empty();
            }

            JsonNode scores = document.path("confidenceScores");
            return Optional.of(new AzureLanguageInsight(
                    document.path("sentiment").asText("unknown"),
                    Map.of(
                            "positive", scores.path("positive").asDouble(0),
                            "neutral", scores.path("neutral").asDouble(0),
                            "negative", scores.path("negative").asDouble(0)
                    )
            ));
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }
}
