package kz.github.rolandp.libraryapiservice.config;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {
    private final KafkaProducerProps kafkaProducerProps;

    @Bean
    public ProducerFactory<String, String> kafkaProducerFactory() {
        val configs = new HashMap<String, Object>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerProps.getBootstrapAddress());
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerProps.getKeySerializer());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerProps.getValueSerializer());
        configs.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerProps.getRetries());
        configs.put(ProducerConfig.ACKS_CONFIG, kafkaProducerProps.getAcks());
        configs.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerProps.getLingerMs());
        configs.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerProps.getBatchSize());
        configs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    @DependsOn("kafkaProducerFactory")
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(kafkaProducerFactory());
    }
}