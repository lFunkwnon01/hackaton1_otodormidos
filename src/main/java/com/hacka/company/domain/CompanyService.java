package com.hacka.company.domain;


import com.hacka.company.Repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company createCompany(Company company) {
        company.setActivo(true);
        company.setFechaAfiliacion(LocalDate.now());
        return companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    public Optional<Company> updateCompany(Long id, Company updatedCompany) {
        return companyRepository.findById(id)
                .map(company -> {
                    company.setNombre(updatedCompany.getNombre());
                    company.setRuc(updatedCompany.getRuc());
                    company.setFechaAfiliacion(updatedCompany.getFechaAfiliacion());
                    company.setActivo(updatedCompany.isActivo());
                    return companyRepository.save(company);
                });
    }

    public Optional<Company> changeCompanyStatus(Long id, boolean activo) {
        return companyRepository.findById(id)
                .map(company -> {
                    company.setActivo(activo);
                    return companyRepository.save(company);
                });
    }

    // Placeholder para el reporte de consumo
    public String getCompanyConsumption(Long id) {
        // Aquí irá la lógica real de consumo
        return "Reporte de consumo para la empresa " + id;
    }
}