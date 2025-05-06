package com.hacka.CompanyRestriction.Domain;

import com.hacka.company.Domain.Company;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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