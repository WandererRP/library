package kz.github.rolandp.notificationservice.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationListener {

    @KafkaListener(topics = "author-event-topic", groupId = "library-group", containerFactory = "kafkaListenerContainerFactory")
    public void listenAuthorEvents(String message) {
        log.info("New author event received: {}", message);
    }

    @KafkaListener(topics = "book-event-topic", groupId = "library-group", containerFactory = "kafkaListenerContainerFactory")
    public void listenBookEvents(String message) {
        log.info("New book event received: {}", message);
    }
}