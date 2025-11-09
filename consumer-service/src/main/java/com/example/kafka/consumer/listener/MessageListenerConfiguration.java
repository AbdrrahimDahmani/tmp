package com.example.kafka.consumer.listener;

import com.example.kafka.common.MessageEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class MessageListenerConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MessageListenerConfiguration.class);

    @Bean
    public Consumer<MessageEnvelope> messageLogger(MessageStore messageStore) {
        return envelope -> {
            log.info("Received message type={} id={} from {}", envelope.type(), envelope.id(), envelope.source());
            messageStore.add(envelope);
        };
    }
}
