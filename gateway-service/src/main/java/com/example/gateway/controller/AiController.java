package com.example.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final RestTemplate restTemplate = new RestTemplate();

    // this ai-service URL should match the service name in docker-compose
    private final String aiServiceUrl = "http://ai-service:5000/api/ai/generate";

    @PostMapping("/generate")
    public ResponseEntity<?> generate(@RequestBody Map<String, String> body) {
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(aiServiceUrl, body, Map.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to connect to AI service", "details", e.getMessage()));
        }
    }
}