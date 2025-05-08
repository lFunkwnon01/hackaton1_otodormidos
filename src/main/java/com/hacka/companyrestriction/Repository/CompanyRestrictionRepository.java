package com.hacka.companyrestriction.Repository;


import com.hacka.companyrestriction.Domain.CompanyRestriction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRestrictionRepository extends JpaRepository<CompanyRestriction, Long> {
    List<CompanyRestriction> findByCompanyId(Long companyId);
}