package com.example;

import brave.Tracer;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
@SpringBootApplication
@AllArgsConstructor
@Slf4j
public class NotificationServiceApplication {
    private final ObservationRegistry observationRegistry;
    private final Tracer tracer;
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
        //send out notification
        Observation.createNotStarted("on-message", this.observationRegistry).observe(() -> {
            log.info("Got message <{}>", orderPlacedEvent);
            log.info("TraceId- {}, Received Notification for Order - {}", this.tracer.currentSpan().context().traceId(),
                    orderPlacedEvent.getOrderNumber());
        });
    }
}
