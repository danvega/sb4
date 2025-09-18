package dev.danvega.sb4.jms_client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class NotificationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);
    private final AtomicInteger messageCount = new AtomicInteger(0);

    @JmsListener(destination = "notification-queue")
    public void processNotification(NotificationMessage notification) {
        logger.info("Received notification: {}", notification);

        messageCount.incrementAndGet();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Processing interrupted", e);
        }

        logger.info("Processed notification with ID: {} (Total processed: {})",
                   notification.id(), messageCount.get());
    }

    public int getMessageCount() {
        return messageCount.get();
    }
}