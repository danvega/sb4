# Core Resilience Features

Spring Boot 4 includes **built-in resilience features** powered by Spring Framework 7,no external libraries like 
Spring Retry needed!

## Overview

Spring Framework 7 brings enterprise-grade resilience patterns directly into the core framework. 
With simple annotations, you can add retry logic and concurrency control to any method.

### Key Concepts

- **Zero external dependencies**: Built into Spring Framework 7 core
- **`@Retryable`**: Automatically retry failed methods with configurable backoff
- **`@ConcurrencyLimit`**: Control concurrent executions to prevent resource exhaustion
- **Exponential backoff**: Smart delay strategies (1s, 2s, 4s, 8s...)
- **Jitter support**: Prevent thundering herd problems

## Resources

### GitHub Repository
https://github.com/danvega/quick-bytes

### Video Tutorial
https://youtu.be/CT1wGTwOfg0

### Blog Post

### Official Documentation
- [Spring Framework Resilience](https://docs.spring.io/spring-framework/reference/core/resilience.html)

## Example Usage

### @Retryable with Exponential Backoff
```java
@Retryable(
    maxRetries = 4,
    delay = 500,
    multiplier = 2.0,  // 500ms, 1s, 2s, 4s
    maxDelay = 5000,
    jitter = 100       // Add randomness
)
public String fetchData(String id) {
    // Method that might fail - will retry with increasing delays
}
```

### @ConcurrencyLimit
```java
@ConcurrencyLimit(2)
public String performHeavyOperation(String taskId) {
    // Only 2 of these can run at the same time
    // Additional requests will queue and wait
}
```

### Combined Patterns
```java
@ConcurrencyLimit(1)
@Retryable(maxRetries = 2, delay = 1000)
public String criticalOperation(String operationId) {
    // Single-threaded execution with automatic retry
}
```

### Configuration
```java
@Configuration
@EnableResilientMethods
public class ResilienceConfig {
    // Enables @Retryable and @ConcurrencyLimit
}
```

## Why This Is Special

Unlike previous Spring Boot versions that required external libraries, **Spring Boot 4 includes these resilience features as part of the core framework**:

- Smaller dependencies
- Better integration with the Spring ecosystem
- Consistent configuration and behavior
- No version compatibility issues
- Enterprise-grade resilience out of the box
