# Spring Framework 7 Programmatic Bean Registration

This package demonstrates the new **BeanRegistrar** interface introduced in Spring Framework 7, which provides a clean and simple way to register beans programmatically.

## What is Programmatic Bean Registration?

Programmatic bean registration allows you to dynamically register beans at runtime based on configuration, environment conditions, or other runtime factors. This is useful when:

- You need to conditionally register beans based on properties
- The bean type or configuration depends on runtime conditions
- You're building frameworks or libraries that need flexible bean registration
- Traditional `@Component` or `@Bean` annotations aren't sufficient

## The New BeanRegistrar Interface

Spring Framework 7 introduces the `BeanRegistrar` interface, which simplifies programmatic bean registration:

```java
public class MessageServiceRegistrar implements BeanRegistrar {
    @Override
    public void register(BeanRegistry registry, Environment env) {
        // Register beans programmatically
        registry.registerBean("myBean", MyClass.class, spec -> spec
                .description("Dynamically registered bean"));
    }
}
```

## Demo Overview

This demo shows a simple message service system that registers different implementations based on configuration:

- **EmailMessageService** - Sends email messages (📧)
- **SmsMessageService** - Sends SMS messages (📱)
- **MessageServiceRegistrar** - Dynamically chooses which service to register
- **MessageController** - REST endpoint to test the registered service

## How It Works

### 1. Configuration Property
The `app.message-type` property determines which service gets registered:

```yaml
# application.yml
app:
  message-type: email  # or "sms"
```

### 2. BeanRegistrar Implementation
The `MessageServiceRegistrar` reads the configuration and registers the appropriate service:

```java
@Override
public void register(BeanRegistry registry, Environment env) {
    String messageType = env.getProperty("app.message-type", "email");

    switch (messageType.toLowerCase()) {
        case "email" -> registry.registerBean("messageService", EmailMessageService.class,
            spec -> spec.description("Email service via BeanRegistrar"));
        case "sms" -> registry.registerBean("messageService", SmsMessageService.class,
            spec -> spec.description("SMS service via BeanRegistrar"));
    }
}
```

### 3. Configuration Class
Simply import the BeanRegistrar using `@Import`:

```java
@Configuration
@Import(MessageServiceRegistrar.class)
public class ModernConfig {
    // Other bean definitions here
}
```

### 4. Usage
The dynamically registered bean can be injected like any other Spring bean:

```java
@RestController
public class MessageController {
    private final MessageService messageService;  // Injected automatically

    @GetMapping("/api/messages")
    public MessageResponse sendMessage() {
        return new MessageResponse(
            messageService.getMessage(),
            messageService.getServiceType()
        );
    }
}
```

## Testing the Demo

1. **Start the application**: `./mvnw spring-boot:run`

2. **Test with email service** (default):
   ```bash
   curl http://localhost:8080/api/messages
   # Returns: {"message":"📧 Email message sent at 2025-09-17T16:01:50.919778","serviceType":"EMAIL"}
   ```

3. **Test with SMS service**: Change `app.message-type: sms` in `application.yml` and restart

## Key Benefits

✅ **Cleaner than before**: No more verbose `BeanDefinitionRegistryPostProcessor`
✅ **Runtime flexibility**: Register different beans based on conditions
✅ **Type-safe**: Full IDE support and compile-time checking
✅ **Modern syntax**: Uses lambda expressions and method chaining
✅ **AOT compatible**: Works with GraalVM native images

## Comparison with Traditional Approaches

| Approach | Complexity | Flexibility | Modern |
|----------|------------|-------------|---------|
| `@Component` / `@Bean` | Low | Low | ✅ |
| `BeanDefinitionRegistryPostProcessor` | High | High | ❌ |
| **BeanRegistrar** (SF7) | Low | High | ✅ |

The new `BeanRegistrar` interface gives you the flexibility of programmatic registration with the simplicity of modern Spring Framework APIs.