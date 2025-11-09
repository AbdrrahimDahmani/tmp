package com.example.kafka.supplier;

import com.example.kafka.common.MessageEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

@Configuration
public class HeartbeatSupplierConfiguration {

    private static final Logger log = LoggerFactory.getLogger(HeartbeatSupplierConfiguration.class);

    @Value("${app.supplier.source:heartbeat-generator}")
    private String source;

    @Bean
    public Supplier<MessageEnvelope> heartbeatSupplier() {
        return () -> {
            MessageEnvelope envelope = new MessageEnvelope(
                    UUID.randomUUID(),
                    source,
                    "heartbeat",
                    "alive",
                    Instant.now()
            );
            log.debug("Sending heartbeat id={}", envelope.id());
            return envelope;
        };
    }
}
