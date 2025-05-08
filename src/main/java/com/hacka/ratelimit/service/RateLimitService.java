package com.hacka.ratelimit.service;

import com.hacka.user.domain.User;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {
    private final Map<String, UsageWindow> userUsage;
    private final Map<String, UsageWindow> companyUsage;

    public RateLimitService() {
        this.userUsage = new ConcurrentHashMap<>();
        this.companyUsage = new ConcurrentHashMap<>();
    }

    /**
     * Check if a request can be processed based on user and company limits
     */
    public boolean canProcessRequest(User user, String modelType, int tokenCount) {
        String userKey = generateUserKey(user.getId(), modelType);
        String companyKey = generateCompanyKey(user.getCompany().getId(), modelType);

        synchronized (this) {
            cleanExpiredWindows();
            return checkUserLimit(userKey, user.getId(), modelType, tokenCount) &&
                   checkCompanyLimit(companyKey, user.getCompany().getId(), modelType, tokenCount);
        }
    }

    /**
     * Record token consumption for both user and company
     */
    public void recordUsage(User user, String modelType, int tokenCount) {
        String userKey = generateUserKey(user.getId(), modelType);
        String companyKey = generateCompanyKey(user.getCompany().getId(), modelType);

        synchronized (this) {
            recordTokenUsage(userKey, tokenCount);
            recordTokenUsage(companyKey, tokenCount);
        }
    }

    private boolean checkUserLimit(String key, Long userId, String modelType, int tokenCount) {
        UsageWindow window = userUsage.computeIfAbsent(key, k -> new UsageWindow());
        return window.canAddTokens(tokenCount);
    }

    private boolean checkCompanyLimit(String key, Long companyId, String modelType, int tokenCount) {
        UsageWindow window = companyUsage.computeIfAbsent(key, k -> new UsageWindow());
        return window.canAddTokens(tokenCount);
    }

    private void recordTokenUsage(String key, int tokenCount) {
        UsageWindow window = userUsage.get(key);
        if (window != null) {
            window.addTokens(tokenCount);
        }
    }

    private void cleanExpiredWindows() {
        Instant now = Instant.now();
        userUsage.entrySet().removeIf(entry -> entry.getValue().isExpired(now));
        companyUsage.entrySet().removeIf(entry -> entry.getValue().isExpired(now));
    }

    private String generateUserKey(Long userId, String modelType) {
        return "user:" + userId + ":" + modelType;
    }

    private String generateCompanyKey(Long companyId, String modelType) {
        return "company:" + companyId + ":" + modelType;
    }

    private static class UsageWindow {
        private Instant windowStart;
        private int tokenCount;
        private int requestCount;

        public UsageWindow() {
            this.windowStart = Instant.now();
            this.tokenCount = 0;
            this.requestCount = 0;
        }

        public boolean canAddTokens(int tokens) {
            if (isExpired(Instant.now())) {
                reset();
                return true;
            }
            return true; // Implement actual limit checking based on UserLimit/CompanyRestriction
        }

        public void addTokens(int tokens) {
            if (isExpired(Instant.now())) {
                reset();
            }
            this.tokenCount += tokens;
            this.requestCount++;
        }

        public boolean isExpired(Instant now) {
            return now.isAfter(windowStart.plus(Duration.ofHours(24)));
        }

        private void reset() {
            this.windowStart = Instant.now();
            this.tokenCount = 0;
            this.requestCount = 0;
        }
    }
}