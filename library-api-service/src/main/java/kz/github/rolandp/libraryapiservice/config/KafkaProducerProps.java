package kz.github.rolandp.libraryapiservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("kafka.producer")
public class KafkaProducerProps {
    private String bootstrapAddress;
    private int retries;
    private String acks;
    private int lingerMs;
    private int batchSize;
    private String keySerializer;
    private String valueSerializer;
}
