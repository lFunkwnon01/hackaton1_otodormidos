package com.hacka.AIRequest.Repository;

import com.hacka.AIRequest.Domain.AIRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AIRequestRepository extends JpaRepository<AIRequest, Long> {
    // Cambia findByUserId → findByUser_Id (JPA busca por el ID del User relacionado)
    List<AIRequest> findByUserId(Long userId);

    // Cambia findByCompanyId → findByCompany_Id (JPA busca por el ID de la Company relacionada)
    List<AIRequest> findByCompanyId(Long companyId);
}