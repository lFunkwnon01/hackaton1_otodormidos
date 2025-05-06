package com.hacka.company.Repository;


import com.hacka.company.Domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    // Puedes agregar métodos personalizados si los necesitas
}
