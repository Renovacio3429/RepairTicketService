package org.repair.ticket.configuration.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topic.name:repair-request-events}")
    private String topicName;

    @Value("${kafka.topic.partitions:3}")
    private int partitions;

    @Value("${kafka.topic.replicas:1}")
    private short replicas;

    @Value("${kafka.topic.retention-ms:604800000}")
    private long retentionMs;

    @Bean
    public NewTopic repairRequestEventsTopic() {
        return new NewTopic(topicName, partitions, replicas)
                .configs(Map.of(
                        "retention.ms", String.valueOf(retentionMs),
                        "cleanup.policy", "delete"
                ));
    }
}
