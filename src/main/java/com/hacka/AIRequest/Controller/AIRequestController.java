package com.hacka.AIRequest.Controller;


import com.hacka.AIRequest.Domain.AIRequest;
import com.hacka.AIRequest.Domain.AIRequestService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Setter
@Getter
@RestController
@RequestMapping("/api/ai")
public class AIRequestController {

    private final AIRequestService aiRequestService;

    public AIRequestController(AIRequestService aiRequestService) {
        this.aiRequestService = aiRequestService;
    }

    // Realizar una consulta a un modelo de chat
    @PostMapping("/chat")
    public ResponseEntity<AIRequest> chat(@RequestBody AIRequest aiRequest) {
        // Aquí deberías integrar la lógica real de consulta al modelo de IA
        aiRequest.setFechaHora(java.time.LocalDateTime.now());
        return ResponseEntity.ok(aiRequestService.saveRequest(aiRequest));
    }

    // Realizar una solicitud de completado de texto
    @PostMapping("/completion")
    public ResponseEntity<AIRequest> completion(@RequestBody AIRequest aiRequest) {
        aiRequest.setFechaHora(java.time.LocalDateTime.now());
        return ResponseEntity.ok(aiRequestService.saveRequest(aiRequest));
    }

    // Realizar una consulta a un modelo multimodal (con imagen)
    @PostMapping("/multimodal")
    public ResponseEntity<AIRequest> multimodal(@RequestBody AIRequest aiRequest) {
        aiRequest.setFechaHora(java.time.LocalDateTime.now());
        return ResponseEntity.ok(aiRequestService.saveRequest(aiRequest));
    }

    // Obtener historial de solicitudes del usuario
    @GetMapping("/history")
    public ResponseEntity<List<AIRequest>> getHistory(@RequestParam Long userId) {
        return ResponseEntity.ok(aiRequestService.getRequestsByUser(userId));
    }

    // Obtener lista de modelos disponibles para el usuario (placeholder)
    @GetMapping("/models")
    public ResponseEntity<List<String>> getModels() {
        // Aquí deberías retornar la lista real de modelos disponibles
        return ResponseEntity.ok(List.of("OpenAI", "Meta", "DeepSpeak", "Multimodal"));
    }
}