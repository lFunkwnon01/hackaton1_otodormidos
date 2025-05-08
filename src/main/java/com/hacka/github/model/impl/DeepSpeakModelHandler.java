package com.hacka.github.model.impl;

import com.hacka.AIRequest.Domain.AIRequest;
import com.hacka.github.model.GitHubModelHandler;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class DeepSpeakModelHandler implements GitHubModelHandler {
    private static final String MODEL_TYPE = "DeepSpeak";
    private static final String[] SUPPORTED_MODELS = {"deepspeak-v1", "deepspeak-v2"};

    @Override
    public AIRequest processRequest(AIRequest request) {
        // TODO: Implement actual DeepSpeak API integration
        request.setStatus("COMPLETED");
        request.setFechaHora(LocalDateTime.now());
        request.setModeloUtilizado(MODEL_TYPE);
        
        // Handle audio/speech input if file is present
        if (request.getNombreArchivo() != null && !request.getNombreArchivo().isEmpty()) {
            // TODO: Implement speech-to-text processing
            request.setResponse("Processed audio input with DeepSpeak model");
        } else {
            request.setResponse("Processed text input with DeepSpeak model");
        }
        
        request.setTokensConsumidos(calculateTokens(request.getPrompt()));
        return request;
    }

    @Override
    public int calculateTokens(String prompt) {
        // DeepSpeak uses specialized tokenization for speech
        // This is a simplified estimation
        return (int) (prompt.length() / 3);
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