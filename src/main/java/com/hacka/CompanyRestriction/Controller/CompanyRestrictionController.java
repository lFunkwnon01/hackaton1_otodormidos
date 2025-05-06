package com.hacka.CompanyRestriction.Controller;


import com.hacka.CompanyRestriction.Domain.CompanyRestriction;
import com.hacka.CompanyRestriction.Domain.CompanyRestrictionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company/restrictions")
public class CompanyRestrictionController {

    private final CompanyRestrictionService restrictionService;

    public CompanyRestrictionController(CompanyRestrictionService restrictionService) {
        this.restrictionService = restrictionService;
    }

    // Crear nueva restricción de modelo para la empresa
    @PostMapping
    public ResponseEntity<CompanyRestriction> createRestriction(@RequestBody CompanyRestriction restriction) {
        return ResponseEntity.ok(restrictionService.createRestriction(restriction));
    }

    // Listar todas las restricciones de la empresa
    @GetMapping
    public ResponseEntity<List<CompanyRestriction>> getRestrictionsByCompany(@RequestParam Long companyId) {
        return ResponseEntity.ok(restrictionService.getRestrictionsByCompany(companyId));
    }

    // Actualizar una restricción existente
    @PutMapping("/{id}")
    public ResponseEntity<CompanyRestriction> updateRestriction(@PathVariable Long id, @RequestBody CompanyRestriction restriction) {
        return restrictionService.updateRestriction(id, restriction)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar una restricción
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestriction(@PathVariable Long id) {
        restrictionService.deleteRestriction(id);
        return ResponseEntity.noContent().build();
    }
}
