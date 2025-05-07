package com.hacka.AIRequest.Controller;

import com.hacka.AIRequest.Domain.AIRequest;
import com.hacka.AIRequest.Domain.AIRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AIRequestController {

    private final AIRequestService aiRequestService;

    public AIRequestController(AIRequestService aiRequestService) {
        this.aiRequestService = aiRequestService;
    }

    @PostMapping("/chat")
    public ResponseEntity<AIRequest> chat(@RequestBody AIRequest aiRequest) {
        aiRequest.setModeloUtilizado(aiRequest.getModelId());
        aiRequest.setFechaHora(java.time.LocalDateTime.now());
        return ResponseEntity.ok(aiRequestService.processAIRequest(aiRequest));
    }

    @PostMapping("/completion")
    public ResponseEntity<AIRequest> completion(@RequestBody AIRequest aiRequest) {
        aiRequest.setModeloUtilizado(aiRequest.getModelId());
        aiRequest.setFechaHora(java.time.LocalDateTime.now());
        return ResponseEntity.ok(aiRequestService.processAIRequest(aiRequest));
    }

    @PostMapping("/multimodal")
    public ResponseEntity<AIRequest> multimodal(@RequestBody AIRequest aiRequest) {
        aiRequest.setModeloUtilizado(aiRequest.getModelId());
        aiRequest.setFechaHora(java.time.LocalDateTime.now());
        return ResponseEntity.ok(aiRequestService.processAIRequest(aiRequest));
    }

    @GetMapping("/history")
    public ResponseEntity<List<AIRequest>> getHistory(@RequestParam Long userId) {
        return ResponseEntity.ok(aiRequestService.getRequestsByUser(userId));
    }

    @GetMapping("/models")
    public ResponseEntity<List<String>> getModels() {
        return ResponseEntity.ok(List.of(
                "openai/gpt-4.1",
                "meta/llama-3-70b",
                "deepseek/deepseek-chat",
                "microsoft/kosmos-2"
        ));
    }
}