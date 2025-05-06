package com.hacka.company.Controller;


import com.hacka.company.Domain.Company;
import com.hacka.company.Domain.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        return ResponseEntity.ok(companyService.createCompany(company));
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        return companyService.getCompanyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company updatedCompany) {
        return companyService.updateCompany(id, updatedCompany)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Company> changeCompanyStatus(@PathVariable Long id, @RequestParam boolean activo) {
        return companyService.changeCompanyStatus(id, activo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/consumption")
    public ResponseEntity<String> getCompanyConsumption(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyConsumption(id));
    }
}