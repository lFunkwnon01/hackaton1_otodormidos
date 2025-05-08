package com.hacka.github.model;

import com.hacka.AIRequest.Domain.AIRequest;
import org.springframework.stereotype.Component;

public interface GitHubModelHandler {
    /**
     * Process a request using the specific model implementation
     * @param request The AI request to process
     * @return The processed AIRequest with response and token consumption
     */
    AIRequest processRequest(AIRequest request);

    /**
     * Calculate token consumption for a given prompt
     * @param prompt The input prompt
     * @return Estimated token count
     */
    int calculateTokens(String prompt);

    /**
     * Get the model type identifier
     * @return Model type (e.g., "OpenAI", "Meta", "DeepSpeak")
     */
    String getModelType();

    /**
     * Check if this handler can process the given model ID
     * @param modelId The model identifier
     * @return true if this handler can process the model
     */
    boolean supportsModel(String modelId);
}