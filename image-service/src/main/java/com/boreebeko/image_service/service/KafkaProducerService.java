package com.boreebeko.image_service.service;

import com.boreebeko.image_service.service.event.ImageUploadedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    private KafkaTemplate<String, ImageUploadedEvent> kafkaTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public KafkaProducerService(KafkaTemplate<String, ImageUploadedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String upload(ImageUploadedEvent event) {

        String eventId = UUID.randomUUID().toString();

        CompletableFuture<SendResult<String, ImageUploadedEvent>> future = kafkaTemplate.send("image-uploaded-event-topic", eventId, event);
        future.whenComplete(
                (result, exception) -> {
                    if (exception != null) {
                        LOGGER.error("Failed to send event-message: {}", exception.getMessage());
                    }
                    else {
                        LOGGER.info("Event-message send successfully: {}", result.getRecordMetadata());
                    }
                }
        );

        return eventId;
    }
}
