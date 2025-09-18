package dev.danvega.sb4.bean_registration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = ModernConfig.class)
@TestPropertySource(properties = "app.message-type=email")
class EmailMessageServiceRegistrationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void shouldRegisterEmailMessageService() {
        MessageService messageService = context.getBean("messageService", MessageService.class);

        assertThat(messageService).isInstanceOf(EmailMessageService.class);
        assertThat(messageService.getServiceType()).isEqualTo("EMAIL");
        assertThat(messageService.getMessage()).contains("📧 Email message sent");
    }
}

@SpringBootTest
@ContextConfiguration(classes = ModernConfig.class)
@TestPropertySource(properties = "app.message-type=sms")
class SmsMessageServiceRegistrationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void shouldRegisterSmsMessageService() {
        MessageService messageService = context.getBean("messageService", MessageService.class);

        assertThat(messageService).isInstanceOf(SmsMessageService.class);
        assertThat(messageService.getServiceType()).isEqualTo("SMS");
        assertThat(messageService.getMessage()).contains("📱 SMS message sent");
    }
}

@SpringBootTest
@ContextConfiguration(classes = ModernConfig.class)
class DefaultMessageServiceRegistrationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void shouldDefaultToEmailWhenNoPropertySet() {
        MessageService messageService = context.getBean("messageService", MessageService.class);

        assertThat(messageService).isInstanceOf(EmailMessageService.class);
        assertThat(messageService.getServiceType()).isEqualTo("EMAIL");
    }
}