package com.example.kafka.web.service;

import com.example.kafka.common.AnalyticsResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AnalyticsStreamService {

    private final ReactiveKafkaConsumerTemplate<String, String> consumerTemplate;
    private final ObjectMapper objectMapper;

    public AnalyticsStreamService(ReactiveKafkaConsumerTemplate<String, String> consumerTemplate, ObjectMapper objectMapper) {
        this.consumerTemplate = consumerTemplate;
        this.objectMapper = objectMapper;
    }

    public Flux<AnalyticsResult> streamAnalytics() {
        return consumerTemplate.receiveAutoAck()
                .map(receiverRecord -> receiverRecord.value())
                .map(this::deserialize)
                .onErrorContinue((throwable, o) -> {
                    // ignore malformed payloads to keep the stream alive
                });
    }

    private AnalyticsResult deserialize(String json) {
        try {
            return objectMapper.readValue(json, AnalyticsResult.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Unable to parse analytics payload", e);
        }
    }
}
