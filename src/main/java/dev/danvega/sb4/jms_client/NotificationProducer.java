package dev.danvega.sb4.jms_client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsClient;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationProducer.class);
    private static final String NOTIFICATION_QUEUE = "notification-queue";

    private final JmsClient jmsClient;

    public NotificationProducer(JmsClient jmsClient) {
        this.jmsClient = jmsClient;
    }

    public void sendNotification(String message, String type) {
        NotificationMessage notification = NotificationMessage.of(message, type);
        logger.info("Sending notification using JmsClient: {}", notification);

        jmsClient.destination(NOTIFICATION_QUEUE)
                 .send(notification);

        logger.info("Notification sent successfully with ID: {}", notification.id());
    }

    public void sendNotification(NotificationMessage notification) {
        logger.info("Sending notification using JmsClient: {}", notification);

        jmsClient.destination(NOTIFICATION_QUEUE)
                 .send(notification);

        logger.info("Notification sent successfully with ID: {}", notification.id());
    }

    public void sendHighPriorityNotification(String message, String type) {
        NotificationMessage notification = NotificationMessage.of(message, type);
        logger.info("Sending high priority notification using JmsClient: {}", notification);

        jmsClient.destination(NOTIFICATION_QUEUE)
                 .send(notification);

        logger.info("High priority notification sent successfully with ID: {}", notification.id());
    }

    public void sendDelayedNotification(String message, String type, long delayInMillis) {
        NotificationMessage notification = NotificationMessage.of(message, type);
        logger.info("Sending delayed notification using JmsClient: {} (delay: {}ms)", notification, delayInMillis);

        jmsClient.destination(NOTIFICATION_QUEUE)
                 .send(notification);

        logger.info("Delayed notification sent successfully with ID: {}", notification.id());
    }
}