package dev.danvega.sb4.jms_client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JmsControllerTest {

    @Mock
    private NotificationProducer notificationProducer;

    @Mock
    private NotificationConsumer notificationConsumer;

    private JmsController controller;

    @BeforeEach
    void setUp() {
        controller = new JmsController(notificationProducer, notificationConsumer);
    }


    @Test
    void sendNotification_ShouldReturnSuccessResponse() {
        var request = new JmsController.NotificationRequest("Test message", "info");

        ResponseEntity<Map<String, String>> response = controller.sendNotification(request);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).containsEntry("status", "Message sent successfully using JmsClient");
        assertThat(response.getBody()).containsEntry("message", "Test message");
        assertThat(response.getBody()).containsEntry("type", "info");

        verify(notificationProducer).sendNotification("Test message", "info");
    }

    @Test
    void sendHighPriorityNotification_ShouldReturnSuccessResponse() {
        var request = new JmsController.NotificationRequest("Urgent message", "alert");

        ResponseEntity<Map<String, String>> response = controller.sendHighPriorityNotification(request);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).containsEntry("status", "High priority message sent successfully using JmsClient");
        assertThat(response.getBody()).containsEntry("message", "Urgent message");
        assertThat(response.getBody()).containsEntry("type", "alert");
        assertThat(response.getBody()).containsEntry("note", "Priority handling via basic JmsClient API");

        verify(notificationProducer).sendHighPriorityNotification("Urgent message", "alert");
    }

    @Test
    void sendDelayedNotification_ShouldReturnSuccessResponse() {
        var request = new JmsController.DelayedNotificationRequest("Delayed message", "reminder", 5000L);

        ResponseEntity<Map<String, Object>> response = controller.sendDelayedNotification(request);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).containsEntry("status", "Delayed message sent successfully using JmsClient");
        assertThat(response.getBody()).containsEntry("message", "Delayed message");
        assertThat(response.getBody()).containsEntry("type", "reminder");
        assertThat(response.getBody()).containsEntry("delayInMillis", 5000L);
        assertThat(response.getBody()).containsEntry("note", "Delay handling via basic JmsClient API");

        verify(notificationProducer).sendDelayedNotification("Delayed message", "reminder", 5000L);
    }

    @Test
    void getStatus_ShouldReturnSystemStatus() {
        when(notificationConsumer.getMessageCount()).thenReturn(42);

        ResponseEntity<Map<String, Object>> response = controller.getStatus();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).containsEntry("totalProcessed", 42);
        assertThat(response.getBody()).containsEntry("status", "Consumer is running");
        assertThat(response.getBody()).containsEntry("jmsClient", "Spring Boot 4 JmsClient in use");

        verify(notificationConsumer).getMessageCount();
    }
}