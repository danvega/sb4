package dev.danvega.sb4.jms_client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/jms")
public class JmsController {

    private final NotificationProducer notificationProducer;
    private final NotificationConsumer notificationConsumer;

    public JmsController(NotificationProducer notificationProducer, NotificationConsumer notificationConsumer) {
        this.notificationProducer = notificationProducer;
        this.notificationConsumer = notificationConsumer;
    }

    @PostMapping("/notifications")
    public ResponseEntity<Map<String, String>> sendNotification(
            @RequestBody NotificationRequest request) {

        notificationProducer.sendNotification(request.message(), request.type());

        return ResponseEntity.ok(Map.of(
            "status", "Message sent successfully using JmsClient",
            "message", request.message(),
            "type", request.type()
        ));
    }

    @PostMapping("/notifications/urgent")
    public ResponseEntity<Map<String, String>> sendHighPriorityNotification(
            @RequestBody NotificationRequest request) {

        notificationProducer.sendHighPriorityNotification(request.message(), request.type());

        return ResponseEntity.ok(Map.of(
            "status", "High priority message sent successfully using JmsClient",
            "message", request.message(),
            "type", request.type(),
            "note", "Priority handling via basic JmsClient API"
        ));
    }

    @PostMapping("/notifications/delayed")
    public ResponseEntity<Map<String, Object>> sendDelayedNotification(
            @RequestBody DelayedNotificationRequest request) {

        notificationProducer.sendDelayedNotification(request.message(), request.type(), request.delayInMillis());

        return ResponseEntity.ok(Map.of(
            "status", "Delayed message sent successfully using JmsClient",
            "message", request.message(),
            "type", request.type(),
            "delayInMillis", request.delayInMillis(),
            "note", "Delay handling via basic JmsClient API"
        ));
    }

    @GetMapping("/notifications/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        return ResponseEntity.ok(Map.of(
            "totalProcessed", notificationConsumer.getMessageCount(),
            "status", "Consumer is running",
            "jmsClient", "Spring Boot 4 JmsClient in use"
        ));
    }

    public record NotificationRequest(String message, String type) {}
    public record DelayedNotificationRequest(String message, String type, long delayInMillis) {}
}