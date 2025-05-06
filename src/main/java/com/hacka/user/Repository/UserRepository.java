package com.hacka.user.Repository;

import com.hacka.user.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByCompanyId(Long companyId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}