package com.example.kafka.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.analytics")
public class AnalyticsTopicProperties {

    /**
     * Kafka topic that contains the analytics results.
     */
    private String topic = "analytics-results";

    /**
     * Consumer group id for the UI.
     */
    private String groupId = "analytics-web";

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
