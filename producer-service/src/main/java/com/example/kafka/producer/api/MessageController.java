package com.example.kafka.producer.api;

import com.example.kafka.common.MessageEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);
    private final StreamBridge streamBridge;

    public MessageController(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @PostMapping
    public ResponseEntity<MessageEnvelope> publish(@Validated @RequestBody MessageRequest request) {
        MessageEnvelope envelope = new MessageEnvelope(
                UUID.randomUUID(),
                request.source() == null || request.source().isBlank() ? "rest-producer" : request.source(),
                request.type(),
                request.payload(),
                Instant.now()
        );
        log.info("Publishing message type={} id={}", envelope.type(), envelope.id());
        streamBridge.send("producer-out-0", envelope);
        return ResponseEntity.accepted().body(envelope);
    }
}
