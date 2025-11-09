package com.example.kafka.producer.api;

import jakarta.validation.constraints.NotBlank;

/**
 * Payload accepted by the REST controller to publish messages to Kafka.
 */
public record MessageRequest(
        @NotBlank(message = "type is required") String type,
        @NotBlank(message = "payload is required") String payload,
        String source
) {
}
