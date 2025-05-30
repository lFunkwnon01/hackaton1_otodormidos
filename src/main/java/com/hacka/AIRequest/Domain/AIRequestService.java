package com.hacka.AIRequest.Domain;

import com.hacka.AIRequest.Repository.AIRequestRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

@Service
public class AIRequestService {

    @Value("${ai.provider.endpoint}")
    private String endpoint;

    @Value("${ai.provider.api-key}")
    private String apiKey;

    private final AIRequestRepository aiRequestRepository;
    private final RestTemplate restTemplate;

    private static final List<String> SUPPORTED_MODELS = Arrays.asList(
            "gpt-4",
            "gpt-3.5-turbo",
            "llama-3-70b",
            "claude-3"
    );

    public AIRequestService(AIRequestRepository aiRequestRepository, RestTemplate restTemplate) {
        this.aiRequestRepository = aiRequestRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public AIRequest processAIRequest(AIRequest request) {
        try {
            validateModel(request.getModelId());
            String response = queryModel(request.getModelId(), request.getPrompt());

            request.setResponse(response);
            request.setStatus("COMPLETED");
            request.setTokensConsumidos(calculateTokens(response));

            return aiRequestRepository.save(request);
        } catch (Exception e) {
            request.setStatus("FAILED");
            request.setResponse("Error: " + e.getMessage());
            return aiRequestRepository.save(request);
        }
    }

    private String queryModel(String modelId, String prompt) {
        // Configuración de headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        String requestBody = String.format(
                "{\"model\": \"%s\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}]}",
                modelId, prompt
        );

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                entity,
                String.class
        );

        return response.getBody();
    }

    private void validateModel(String modelId) {
        if (!SUPPORTED_MODELS.contains(modelId)) {
            throw new IllegalArgumentException("Modelo no soportado: " + modelId);
        }
    }

    private Integer calculateTokens(String text) {
        return text != null ? text.length() / 4 : 0;
    }

    // Métodos para consultas históricas
    public List<AIRequest> getRequestsByUser(Long userId) {
        return aiRequestRepository.findByUserId(userId);
    }

    public List<AIRequest> getRequestsByCompany(Long companyId) {
        return aiRequestRepository.findByCompanyId(companyId);
    }

    public AIRequest saveRequest(AIRequest aiRequest) {
        return aiRequestRepository.save(aiRequest);
    }
}