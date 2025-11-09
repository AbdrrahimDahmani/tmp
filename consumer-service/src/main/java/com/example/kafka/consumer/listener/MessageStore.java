package com.example.kafka.consumer.listener;

import com.example.kafka.common.MessageEnvelope;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Keeps a bounded in-memory list of recently consumed messages for diagnostics.
 */
@Component
public class MessageStore {

    private static final int MAX_SIZE = 100;
    private final Queue<MessageEnvelope> messages = new ArrayDeque<>();

    public synchronized void add(MessageEnvelope envelope) {
        if (messages.size() >= MAX_SIZE) {
            messages.poll();
        }
        messages.offer(envelope);
    }

    public synchronized List<MessageEnvelope> getRecentMessages() {
        return messages.stream().collect(Collectors.toList());
    }
}
