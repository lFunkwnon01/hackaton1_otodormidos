package com.hacka.CompanyRestriction.Repository;


import com.hacka.CompanyRestriction.Domain.CompanyRestriction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRestrictionRepository extends JpaRepository<CompanyRestriction, Long> {
    List<CompanyRestriction> findByCompanyId(Long companyId);
}