package com.example.demo.controller;

import com.example.demo.model.dto.GeminiServiceRequest;
import com.example.demo.model.dto.GeminiServiceResponse;
import com.example.demo.service.GeminiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeminiController {
    private final GeminiService geminiService = new GeminiService();
    private final ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper 인스턴스 생성

    @PostMapping("/api/test")
    public ResponseEntity<GeminiServiceResponse> doPost(@RequestBody GeminiServiceRequest serviceRequest) {
        // 질문에 대한 응답 가져오기
        String answer = geminiService.answerQuestion(serviceRequest.question());

        if (answer != null) {
            // JSON 응답 생성
            GeminiServiceResponse response = new GeminiServiceResponse(answer);
            return ResponseEntity.ok(response);  // 200 OK와 함께 응답 반환
        } else {
            return ResponseEntity.status(500).build();  // 서버 오류 응답
        }
    }

}
