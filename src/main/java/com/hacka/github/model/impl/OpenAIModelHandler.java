package com.hacka.github.model.impl;

import com.hacka.AIRequest.Domain.AIRequest;
import com.hacka.github.model.GitHubModelHandler;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class OpenAIModelHandler implements GitHubModelHandler {
    private static final String MODEL_TYPE = "OpenAI";
    private static final String[] SUPPORTED_MODELS = {"gpt-3.5-turbo", "gpt-4"};

    @Override
    public AIRequest processRequest(AIRequest request) {
        // TODO: Implement actual OpenAI API integration
        request.setStatus("COMPLETED");
        request.setFechaHora(LocalDateTime.now());
        request.setModeloUtilizado(MODEL_TYPE);
        
        // Simulate response and token consumption
        request.setResponse("Simulated OpenAI response");
        request.setTokensConsumidos(calculateTokens(request.getPrompt()));
        
        return request;
    }

    @Override
    public int calculateTokens(String prompt) {
        // Simple estimation: 1 token ≈ 4 characters
        return prompt.length() / 4;
    }

    @Override
    public String getModelType() {
        return MODEL_TYPE;
    }

    @Override
    public boolean supportsModel(String modelId) {
        for (String supportedModel : SUPPORTED_MODELS) {
            if (supportedModel.equalsIgnoreCase(modelId)) {
                return true;
            }
        }
        return false;
    }
}