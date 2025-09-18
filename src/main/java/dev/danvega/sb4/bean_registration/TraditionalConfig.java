package dev.danvega.sb4.bean_registration;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

//@Configuration
public class TraditionalConfig {

    @Bean
    static BeanDefinitionRegistryPostProcessor messageServicePostProcessor(Environment env) {
        return new BeanDefinitionRegistryPostProcessor() {
            @Override
            public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
                String messageType = env.getProperty("app.message-type", "email");

                var beanDefinition = new GenericBeanDefinition();
                beanDefinition.setDescription("Traditional " + messageType + " message service");

                switch (messageType.toLowerCase()) {
                    case "email" -> beanDefinition.setBeanClass(EmailMessageService.class);
                    case "sms" -> beanDefinition.setBeanClass(SmsMessageService.class);
                    default -> throw new IllegalArgumentException("Unknown message type: " + messageType);
                }

                registry.registerBeanDefinition("messageService", beanDefinition);
            }

            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
                // No-op
            }
        };
    }
}