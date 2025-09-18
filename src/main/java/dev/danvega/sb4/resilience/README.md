# Spring Boot 4 Core Resilience Features

Spring Boot 4 includes built-in resilience features powered by Spring Framework 7 - no external libraries needed!

## What This Demo Shows

### @Retryable
Automatically retry failed methods with configurable delays and backoff strategies:
```java
@Retryable(maxAttempts = 3, delay = 1000)
public String saveData(String data) {
    // Method that might fail - will retry up to 3 times with 1s delay
}

// Advanced: Exponential backoff with jitter
@Retryable(
    maxAttempts = 4,
    delay = 500,
    multiplier = 2.0,  // 500ms, 1s, 2s, 4s
    maxDelay = 5000,
    jitter = 100       // Add randomness to prevent thundering herd
)
public String fetchData(String id) {
    // Sophisticated retry with increasing delays
}
```

### @ConcurrencyLimit
Control how many requests run simultaneously to prevent resource exhaustion:
```java
@ConcurrencyLimit(2)
public String performHeavyOperation(String taskId) {
    // Only 2 of these can run at the same time
    // Additional requests will queue and wait
}
```

### Combined Patterns
Mix resilience patterns for robust fault tolerance:
```java
@ConcurrencyLimit(1)           // Serialize access
@Retryable(maxAttempts = 2, delay = 1000)  // Retry on failure
public String criticalOperation(String operationId) {
    // Single-threaded execution with automatic retry
}
```

### Configuration
Enable resilience features with a simple annotation:
```java
@Configuration
@EnableResilientMethods
public class ResilienceConfig {
    // Enables @Retryable and @ConcurrencyLimit
}
```

## Files to Look At

- **`DatabaseService.java`** - Complete service showing all resilience patterns
- **`ResilienceDemoController.java`** - REST endpoints to test each feature
- **`ResilienceConfig.java`** - Configuration to enable resilience features
- **`DatabaseServiceTest.java`** - Tests demonstrating the features work

## Try It Out

1. Start the app: `./mvnw spring-boot:run`
2. Get help: `GET http://localhost:8080/api/resilience/help`
3. Test basic retry: `POST http://localhost:8080/api/resilience/save` with body "test data"
4. Test exponential backoff: `GET http://localhost:8080/api/resilience/fetch/123`
5. Test concurrency limits: `POST http://localhost:8080/api/resilience/test-concurrency?numberOfTasks=5`
6. **Watch the console logs** to see resilience patterns in action!

## Key Features & Benefits

- **Zero external dependencies** - Built into Spring Framework 7 core
- **Declarative approach** - Just add annotations to methods
- **Flexible configuration** - Fine-tune retry behavior and concurrency limits
- **Exponential backoff** - Smart delay strategies (1s, 2s, 4s, 8s...)
- **Jitter support** - Prevent thundering herd problems
- **Exception filtering** - Choose which exceptions trigger retries
- **AOP integration** - Works seamlessly with Spring's aspect-oriented programming
- **Thread-safe** - Safe for concurrent environments
- **Production-ready** - Battle-tested patterns for enterprise applications

## What Makes This Special

Unlike previous Spring Boot versions that required external libraries like Spring Retry or Resilience4j, **Spring Boot 4 includes these resilience features as part of the core framework**. This means:

- Smaller dependencies
- Better integration with Spring ecosystem
- Consistent configuration and behavior
- No version compatibility issues
- Enterprise-grade resilience out of the box

## Learn More

📚 **Official Documentation**: [Spring Framework 7 Core Resilience Features](https://docs.spring.io/spring-framework/reference/7.0-SNAPSHOT/core/resilience.html)

This comprehensive guide covers all resilience annotations, configuration options, and advanced usage patterns.