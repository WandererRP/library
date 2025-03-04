package kz.github.rolandp.libraryapiservice.service;

/**
 * @author Roland Pilpani 03.03.2025
 */
public interface KafkaProducerService {
    void sendMessage(String topic, String message);
}
