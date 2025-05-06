package com.hacka.UserLimit.Domain;

import com.hacka.UserLimit.Repository.UserLimitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserLimitService {

    private final UserLimitRepository userLimitRepository;

    public UserLimitService(UserLimitRepository userLimitRepository) {
        this.userLimitRepository = userLimitRepository;
    }

    public UserLimit createUserLimit(UserLimit userLimit) {
        return userLimitRepository.save(userLimit);
    }

    public List<UserLimit> getUserLimitsByUser(Long userId) {
        return userLimitRepository.findByUserId(userId);
    }

    public Optional<UserLimit> updateUserLimit(Long id, UserLimit updatedLimit) {
        return userLimitRepository.findById(id)
                .map(limit -> {
                    limit.setTipoModelo(updatedLimit.getTipoModelo());
                    limit.setLimiteSolicitudes(updatedLimit.getLimiteSolicitudes());
                    limit.setLimiteTokens(updatedLimit.getLimiteTokens());
                    limit.setVentanaTiempo(updatedLimit.getVentanaTiempo());
                    return userLimitRepository.save(limit);
                });
    }

    public void deleteUserLimit(Long id) {
        userLimitRepository.deleteById(id);
    }
}