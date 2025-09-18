package dev.danvega.sb4.bean_registration;

import org.springframework.beans.factory.BeanRegistrar;
import org.springframework.beans.factory.BeanRegistry;
import org.springframework.core.env.Environment;

/**
 * Demonstrates Spring Framework 7 programmatic bean registration using BeanRegistrar.
 * This is the modern, simplified approach for dynamic bean registration.
 */
public class MessageServiceRegistrar implements BeanRegistrar {

    @Override
    public void register(BeanRegistry registry, Environment env) {
        String messageType = env.getProperty("app.message-type", "email");

        switch (messageType.toLowerCase()) {
            case "email" -> registry.registerBean("messageService", EmailMessageService.class, spec -> spec
                    .description("Email message service registered via BeanRegistrar"));
            case "sms" -> registry.registerBean("messageService", SmsMessageService.class, spec -> spec
                    .description("SMS message service registered via BeanRegistrar"));
            default -> throw new IllegalArgumentException("Unknown message type: " + messageType);
        }
    }
}