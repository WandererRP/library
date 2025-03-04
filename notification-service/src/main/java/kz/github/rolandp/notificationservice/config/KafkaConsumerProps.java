package kz.github.rolandp.notificationservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("kafka.consumer")
public class KafkaConsumerProps {
    private String bootstrapAddress;
    private String groupId;
    private String autoOffsetReset;
    private boolean enableAutoCommit;
    private int maxPollRecords;
    private String keyDeserializer;
    private String valueDeserializer;
}