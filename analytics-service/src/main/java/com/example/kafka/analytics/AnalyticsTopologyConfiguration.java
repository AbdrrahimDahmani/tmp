package com.example.kafka.analytics;

import com.example.kafka.common.AnalyticsResult;
import com.example.kafka.common.MessageEnvelope;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;

@Configuration
public class AnalyticsTopologyConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsTopologyConfiguration.class);

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Function<KStream<String, MessageEnvelope>, KStream<String, String>> messageAnalytics(ObjectMapper objectMapper) {
        JsonSerde<MessageEnvelope> messageSerde = new JsonSerde<>(MessageEnvelope.class);
        messageSerde.ignoreTypeHeaders();

        return input -> input
                .peek((key, value) -> log.debug("Analyzing message type={} id={}", value.type(), value.id()))
                .selectKey((key, value) -> value.type())
                .groupByKey(Grouped.with(Serdes.String(), messageSerde))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(30)))
                .count()
                .toStream()
                .map((Windowed<String> windowedKey, Long count) -> {
                    AnalyticsResult result = new AnalyticsResult(
                            windowedKey.key(),
                            count,
                            Instant.ofEpochMilli(windowedKey.window().start()),
                            Instant.ofEpochMilli(windowedKey.window().end())
                    );
                    try {
                        return KeyValue.pair(windowedKey.key(), objectMapper.writeValueAsString(result));
                    } catch (JsonProcessingException e) {
                        throw new IllegalStateException("Unable to serialize analytics result", e);
                    }
                });
    }
}
