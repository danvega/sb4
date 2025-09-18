package dev.danvega.sb4.jms_client;

import java.time.LocalDateTime;

public record NotificationMessage(
        String id,
        String message,
        String type,
        LocalDateTime timestamp
) {
    public static NotificationMessage of(String message, String type) {
        return new NotificationMessage(
                java.util.UUID.randomUUID().toString(),
                message,
                type,
                LocalDateTime.now()
        );
    }
}