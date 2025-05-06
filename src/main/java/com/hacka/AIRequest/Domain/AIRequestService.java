package com.hacka.AIRequest.Domain;


import com.hacka.AIRequest.Repository.AIRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AIRequestService {

    private final AIRequestRepository aiRequestRepository;

    public AIRequestService(AIRequestRepository aiRequestRepository) {
        this.aiRequestRepository = aiRequestRepository;
    }

    public AIRequest saveRequest(AIRequest aiRequest) {
        return aiRequestRepository.save(aiRequest);
    }

    public List<AIRequest> getRequestsByUser(Long userId) {
        return aiRequestRepository.findByUserId(userId);
    }

    public List<AIRequest> getRequestsByCompany(Long companyId) {
        return aiRequestRepository.findByCompanyId(companyId);
    }

    public Optional<AIRequest> getRequestById(Long id) {
        return aiRequestRepository.findById(id);
    }
}