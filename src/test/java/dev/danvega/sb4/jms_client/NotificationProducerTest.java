package dev.danvega.sb4.jms_client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationProducerTest {

    @Mock
    private JmsClient jmsClient;

    @Mock
    private JmsClient.OperationSpec destinationStep;

    private NotificationProducer producer;

    @BeforeEach
    void setUp() {
        producer = new NotificationProducer(jmsClient);
    }

    @Test
    void sendNotification_ShouldCallJmsClientCorrectly() {
        when(jmsClient.destination(anyString())).thenReturn(destinationStep);

        producer.sendNotification("Test message", "info");

        verify(jmsClient).destination("notification-queue");
        verify(destinationStep).send(any(NotificationMessage.class));
    }

    @Test
    void sendNotificationWithObject_ShouldCallJmsClientCorrectly() {
        when(jmsClient.destination(anyString())).thenReturn(destinationStep);
        NotificationMessage notification = NotificationMessage.of("Test message", "info");

        producer.sendNotification(notification);

        verify(jmsClient).destination("notification-queue");
        verify(destinationStep).send(notification);
    }

    @Test
    void sendHighPriorityNotification_ShouldCallJmsClientCorrectly() {
        when(jmsClient.destination(anyString())).thenReturn(destinationStep);

        producer.sendHighPriorityNotification("Urgent message", "alert");

        verify(jmsClient).destination("notification-queue");
        verify(destinationStep).send(any(NotificationMessage.class));
    }

    @Test
    void sendDelayedNotification_ShouldCallJmsClientCorrectly() {
        when(jmsClient.destination(anyString())).thenReturn(destinationStep);

        producer.sendDelayedNotification("Delayed message", "reminder", 5000L);

        verify(jmsClient).destination("notification-queue");
        verify(destinationStep).send(any(NotificationMessage.class));
    }

    @Test
    void sendNotificationMessage_ShouldHaveCorrectStructure() {
        when(jmsClient.destination(anyString())).thenReturn(destinationStep);
        ArgumentCaptor<NotificationMessage> messageCaptor = ArgumentCaptor.forClass(NotificationMessage.class);

        producer.sendNotification("Test message", "info");

        verify(destinationStep).send(messageCaptor.capture());
        NotificationMessage capturedMessage = messageCaptor.getValue();

        assertThat(capturedMessage.message()).isEqualTo("Test message");
        assertThat(capturedMessage.type()).isEqualTo("info");
        assertThat(capturedMessage.id()).isNotNull();
        assertThat(capturedMessage.timestamp()).isNotNull();
    }
}