package com.bridgelabz.website.exception;

import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> handleFeignException(FeignException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", e.status());
        
        String content = e.contentUTF8();
        String message = "Microservice communication error";
        
        // Try to extract the real message from the content if it's JSON
        if (content != null && content.contains("\"message\":\"")) {
            try {
                int start = content.indexOf("\"message\":\"") + 11;
                int end = content.indexOf("\"", start);
                message = content.substring(start, end);
            } catch (Exception ex) {
                message = "Microservice error: " + e.getMessage();
            }
        } else {
            message = e.getMessage();
        }
        
        error.put("message", message);
        error.put("content", content);
        return ResponseEntity.status(e.status() > 0 ? e.status() : 500).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception e) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", 500);
        error.put("message", "Internal Server Error: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(500).body(error);
    }
}
