package com.hacka.companyrestriction.Domain;


import com.hacka.companyrestriction.Repository.CompanyRestrictionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyRestrictionService {

    private final CompanyRestrictionRepository restrictionRepository;

    public CompanyRestrictionService(CompanyRestrictionRepository restrictionRepository) {
        this.restrictionRepository = restrictionRepository;
    }

    public CompanyRestriction createRestriction(CompanyRestriction restriction) {
        return restrictionRepository.save(restriction);
    }

    public List<CompanyRestriction> getRestrictionsByCompany(Long companyId) {
        return restrictionRepository.findByCompanyId(companyId);
    }

    public Optional<CompanyRestriction> updateRestriction(Long id, CompanyRestriction updatedRestriction) {
        return restrictionRepository.findById(id)
                .map(restriction -> {
                    restriction.setTipoModelo(updatedRestriction.getTipoModelo());
                    restriction.setLimiteSolicitudes(updatedRestriction.getLimiteSolicitudes());
                    restriction.setLimiteTokens(updatedRestriction.getLimiteTokens());
                    restriction.setVentanaTiempo(updatedRestriction.getVentanaTiempo());
                    return restrictionRepository.save(restriction);
                });
    }

    public void deleteRestriction(Long id) {
        restrictionRepository.deleteById(id);
    }
}