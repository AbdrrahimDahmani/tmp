package com.example.kafka.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

/**
 * Canonical message representation shared by the different services.
 */
public record MessageEnvelope(
        @JsonProperty("id") UUID id,
        @JsonProperty("source") String source,
        @JsonProperty("type") String type,
        @JsonProperty("payload") String payload,
        @JsonProperty("timestamp") Instant timestamp
) {
    @JsonCreator
    public MessageEnvelope {
    }
}
