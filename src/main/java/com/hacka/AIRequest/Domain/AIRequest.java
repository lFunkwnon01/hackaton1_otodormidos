package com.hacka.AIRequest.Domain;

import com.hacka.company.Domain.Company;
import com.hacka.user.Domain.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "ai_request")
@Data
public class AIRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelId;  // ID del modelo (ej: "openai/gpt-4.1")
    private String prompt;   // Texto de la consulta

    @Column(length = 2000)
    private String response; // Respuesta del modelo

    private String status = "PENDING"; // PENDING, COMPLETED, FAILED

    private Integer tokensConsumidos;
    private LocalDateTime fechaHora;
    private String nombreArchivo; // Para multimodal

    private String modeloUtilizado; // Nombre legible del modelo

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        AIRequest aiRequest = (AIRequest) o;
        return getId() != null && Objects.equals(getId(), aiRequest.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}