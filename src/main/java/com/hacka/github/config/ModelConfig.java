package com.hacka.github.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "github.models")
public class ModelConfig {
    private Map<String, ModelProperties> models = new HashMap<>();

    public Map<String, ModelProperties> getModels() {
        return models;
    }

    public void setModels(Map<String, ModelProperties> models) {
        this.models = models;
    }

    public static class ModelProperties {
        private String apiKey;
        private String endpoint;
        private int maxTokens;
        private int requestTimeout;

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public int getMaxTokens() {
            return maxTokens;
        }

        public void setMaxTokens(int maxTokens) {
            this.maxTokens = maxTokens;
        }

        public int getRequestTimeout() {
            return requestTimeout;
        }

        public void setRequestTimeout(int requestTimeout) {
            this.requestTimeout = requestTimeout;
        }
    }
}