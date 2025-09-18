package dev.danvega.sb4.bean_registration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Modern Spring Framework 7 configuration using BeanRegistrar.
 * Clean and simple - just import the BeanRegistrar class.
 */
@Configuration
@Import(MessageServiceRegistrar.class)
public class ModernConfig {

    // other bean definitions here

}