package dev.danvega.sb4.jms_client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class JmsIntegrationTest {

    @Autowired
    private NotificationProducer producer;

    @Autowired
    private NotificationConsumer consumer;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.artemis.mode", () -> "embedded");
        registry.add("spring.artemis.embedded.enabled", () -> "true");
        registry.add("spring.artemis.embedded.queues", () -> "notification-queue");
    }

    @Test
    void shouldSendAndReceiveNotification() {
        int initialCount = consumer.getMessageCount();

        producer.sendNotification("Integration test message", "test");

        await().atMost(10, TimeUnit.SECONDS)
               .untilAsserted(() -> {
                   assertThat(consumer.getMessageCount()).isGreaterThan(initialCount);
               });
    }

    @Test
    void shouldSendAndReceiveHighPriorityNotification() {
        int initialCount = consumer.getMessageCount();

        producer.sendHighPriorityNotification("Priority test message", "urgent");

        await().atMost(10, TimeUnit.SECONDS)
               .untilAsserted(() -> {
                   assertThat(consumer.getMessageCount()).isGreaterThan(initialCount);
               });
    }

    @Test
    void shouldSendAndReceiveDelayedNotification() {
        int initialCount = consumer.getMessageCount();

        producer.sendDelayedNotification("Delayed test message", "scheduled", 2000L);

        await().atMost(15, TimeUnit.SECONDS)
               .untilAsserted(() -> {
                   assertThat(consumer.getMessageCount()).isGreaterThan(initialCount);
               });
    }

    @Test
    void shouldSendMultipleNotificationsAndTrackCount() {
        int initialCount = consumer.getMessageCount();
        int messagesToSend = 3;

        for (int i = 0; i < messagesToSend; i++) {
            producer.sendNotification("Message " + i, "bulk-test");
        }

        await().atMost(15, TimeUnit.SECONDS)
               .untilAsserted(() -> {
                   assertThat(consumer.getMessageCount()).isGreaterThanOrEqualTo(initialCount + messagesToSend);
               });
    }
}