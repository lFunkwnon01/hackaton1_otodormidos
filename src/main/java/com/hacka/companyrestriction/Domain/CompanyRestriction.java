package com.hacka.companyrestriction.Domain;

import com.hacka.company.domain.Company;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class CompanyRestriction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoModelo; // Ej: OpenAI, Meta, etc.

    private Integer limiteSolicitudes; // Por ventana de tiempo

    private Integer limiteTokens; // Por ventana de tiempo

    private String ventanaTiempo; // Ej: diario, semanal

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}