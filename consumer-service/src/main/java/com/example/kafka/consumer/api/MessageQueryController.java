package com.example.kafka.consumer.api;

import com.example.kafka.common.MessageEnvelope;
import com.example.kafka.consumer.listener.MessageStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageQueryController {

    private final MessageStore messageStore;

    public MessageQueryController(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    @GetMapping("/recent")
    public List<MessageEnvelope> recentMessages() {
        return messageStore.getRecentMessages();
    }
}
