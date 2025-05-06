package com.hacka.UserLimit.Controller;


import com.hacka.UserLimit.Domain.UserLimit;
import com.hacka.UserLimit.Domain.UserLimitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company/users/{userId}/limits")
public class UserLimitController {

    private final UserLimitService userLimitService;

    public UserLimitController(UserLimitService userLimitService) {
        this.userLimitService = userLimitService;
    }

    // Asignar límite específico a un usuario
    @PostMapping
    public ResponseEntity<UserLimit> createUserLimit(@PathVariable Long userId, @RequestBody UserLimit userLimit) {
        userLimit.getUser().setId(userId); // Asegura la relación
        return ResponseEntity.ok(userLimitService.createUserLimit(userLimit));
    }

    // Listar límites de un usuario
    @GetMapping
    public ResponseEntity<List<UserLimit>> getUserLimits(@PathVariable Long userId) {
        return ResponseEntity.ok(userLimitService.getUserLimitsByUser(userId));
    }

    // Actualizar un límite específico
    @PutMapping("/{limitId}")
    public ResponseEntity<UserLimit> updateUserLimit(@PathVariable Long userId, @PathVariable Long limitId, @RequestBody UserLimit userLimit) {
        userLimit.getUser().setId(userId);
        return userLimitService.updateUserLimit(limitId, userLimit)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un límite específico
    @DeleteMapping("/{limitId}")
    public ResponseEntity<Void> deleteUserLimit(@PathVariable Long userId, @PathVariable Long limitId) {
        userLimitService.deleteUserLimit(limitId);
        return ResponseEntity.noContent().build();
    }
}
