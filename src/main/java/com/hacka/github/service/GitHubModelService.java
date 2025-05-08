package com.hacka.github.service;

import com.hacka.AIRequest.Domain.AIRequest;
import com.hacka.github.model.GitHubModelHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GitHubModelService {
    private final Map<String, GitHubModelHandler> modelHandlers;

    public GitHubModelService(List<GitHubModelHandler> handlers) {
        modelHandlers = new ConcurrentHashMap<>();
        handlers.forEach(handler -> 
            modelHandlers.put(handler.getModelType(), handler));
    }

    /**
     * Process an AI request using the appropriate model handler
     * @param request The AI request to process
     * @return The processed request with response and token consumption
     * @throws IllegalArgumentException if no handler is found for the model
     */
    public AIRequest processRequest(AIRequest request) {
        GitHubModelHandler handler = findHandler(request.getModelId());
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for model: " + request.getModelId());
        }
        return handler.processRequest(request);
    }

    /**
     * Calculate token consumption for a prompt
     * @param modelId The model identifier
     * @param prompt The input prompt
     * @return Estimated token count
     */
    public int calculateTokens(String modelId, String prompt) {
        GitHubModelHandler handler = findHandler(modelId);
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for model: " + modelId);
        }
        return handler.calculateTokens(prompt);
    }

    private GitHubModelHandler findHandler(String modelId) {
        return modelHandlers.values().stream()
                .filter(handler -> handler.supportsModel(modelId))
                .findFirst()
                .orElse(null);
    }
}