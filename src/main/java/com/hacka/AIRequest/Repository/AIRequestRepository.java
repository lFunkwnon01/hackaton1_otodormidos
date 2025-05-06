package com.hacka.AIRequest.Repository;


import com.hacka.AIRequest.Domain.AIRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AIRequestRepository extends JpaRepository<AIRequest, Long> {
    List<AIRequest> findByUserId(Long userId);
    List<AIRequest> findByCompanyId(Long companyId);
}