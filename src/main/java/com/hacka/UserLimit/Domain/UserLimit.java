package com.hacka.UserLimit.Domain;

import com.hacka.user.Domain.User;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoModelo; // Ej: OpenAI, Meta, etc.

    private Integer limiteSolicitudes; // Por ventana de tiempo

    private Integer limiteTokens; // Por ventana de tiempo

    private String ventanaTiempo; // Ej: diario, semanal

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}