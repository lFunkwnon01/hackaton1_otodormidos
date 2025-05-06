package com.hacka.user.Controller;


import com.hacka.user.Domain.User;
import com.hacka.user.Domain.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Crear nuevo usuario para la empresa
    @PostMapping
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    // Listar todos los usuarios de la empresa
    @GetMapping
    public ResponseEntity<List<User>> getUsersByCompany(@RequestParam Long companyId) {
        return ResponseEntity.ok(userService.getUsersByCompany(companyId));
    }

    // Obtener información detallada de un usuario
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar información de usuario
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}