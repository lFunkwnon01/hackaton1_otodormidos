package com.hacka.UserLimit.Repository;


import com.hacka.UserLimit.Domain.UserLimit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserLimitRepository extends JpaRepository<UserLimit, Long> {
    List<UserLimit> findByUserId(Long userId);
}