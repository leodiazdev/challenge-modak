package com.challenge.errorproducer.controller;

import com.challenge.errorproducer.producer.MessageProducer;
import com.challenge.errorproducer.domain.Message;
import com.challenge.errorproducer.exceptions.RateLimitExceededException;
import com.challenge.errorproducer.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class ErrorProducerController {
    private final MessageProducer producer;

    @Autowired
    public ErrorProducerController(MessageProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/send-message")
    public ResponseEntity<ApiResponse> sendMessage(@RequestBody Message message) {
        try {
            producer.sendMessage(message);
            return ResponseEntity.ok(new ApiResponse(true, "Message sent successfully", null));
        } catch (RateLimitExceededException e) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                             .body(new ApiResponse(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ApiResponse(false, "Internal server error", null));
        }
    }
}
