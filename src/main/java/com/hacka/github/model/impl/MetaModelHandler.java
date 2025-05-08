package com.hacka.github.model.impl;

import com.hacka.AIRequest.Domain.AIRequest;
import com.hacka.github.model.GitHubModelHandler;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class MetaModelHandler implements GitHubModelHandler {
    private static final String MODEL_TYPE = "Meta";
    private static final String[] SUPPORTED_MODELS = {"llama-2", "llama-2-70b"};

    @Override
    public AIRequest processRequest(AIRequest request) {
        // TODO: Implement actual Meta API integration
        request.setStatus("COMPLETED");
        request.setFechaHora(LocalDateTime.now());
        request.setModeloUtilizado(MODEL_TYPE);
        
        // Handle multimodal input if file is present
        if (request.getNombreArchivo() != null && !request.getNombreArchivo().isEmpty()) {
            // TODO: Implement multimodal processing
            request.setResponse("Processed multimodal input with Meta model");
        } else {
            request.setResponse("Processed text input with Meta model");
        }
        
        request.setTokensConsumidos(calculateTokens(request.getPrompt()));
        return request;
    }

    @Override
    public int calculateTokens(String prompt) {
        // Meta models typically use different tokenization
        // This is a simplified estimation
        return (int) (prompt.length() / 3.5);
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