# Spring Boot 4 Demo Project

A comprehensive demonstration of Spring Boot 4.0.0-M2 features using Java 24 and Maven.

## Features Demonstrated

### 1. JSpecify Null Safety (`null_safety/`)
JSpecify null safety annotations with package-level @NullMarked annotation.
- **Endpoints**: `/api/users` - User CRUD operations with null safety
- **Key Files**: `package-info.java`, `UserController.java`, `UserService.java`

### 2. HTTP Interface Clients (`http_interface_clients/`)
Declarative HTTP clients using @HttpExchange annotations, replacing RestTemplate/WebClient.
- **Endpoints**: `/api/todos` - Todo operations using interface clients
- **Key Files**: `TodoService.java` (interface), `TodoController.java`
- **External API**: JSONPlaceholder for demo data

### 3. Programmatic Bean Registration (`bean_registration/`)
Programmatic bean registration using ImportBeanDefinitionRegistrar.
- **Endpoints**: `/api/messages` - Message service with different implementations
- **Key Files**: `MessageServiceRegistrar.java`, `ModernConfig.java`
- **Services**: Email and SMS message services

### 4. API Versioning (`api_versioning/`)
Content negotiation-based API versioning with different response models.
- **Endpoints**: `/api/products` - Products with V1/V2 response formats
- **Key Files**: `ProductController.java`, `ProductV1Response.java`, `ProductV2Response.java`
- **Headers**: `Accept: application/vnd.api.v1+json` or `application/vnd.api.v2+json`

### 5. JMS Client (`jms_client/`)
JMS messaging using Apache Artemis with producer and consumer.
- **Endpoints**: `/api/notifications` - Send and receive notifications
- **Key Files**: `NotificationProducer.java`, `NotificationConsumer.java`
- **Queue**: `notification.queue`

### 6. Core Resilience Features (`resilience/`)
Built-in resilience features with @Retryable and @ConcurrencyLimit annotations - no external libraries needed!
- **Endpoints**: `/api/resilience/**` - Database operations demonstrating resilience patterns
- **Key Files**: `DatabaseService.java`, `ResilienceDemoController.java`, `ResilienceConfig.java`
- **Features**: Automatic retry with exponential backoff, concurrency limiting, fault tolerance

## Quick Start

### Prerequisites
- Java 24
- Maven 3.9+

### Running the Application
```bash
./mvnw spring-boot:run
```

### Building
```bash
./mvnw clean compile
./mvnw clean package
```

### Testing
```bash
./mvnw test
./mvnw verify
```

## Technology Stack

- **Java**: 24
- **Spring Boot**: 4.0.0-M2 (milestone release)
- **Build Tool**: Maven with wrapper
- **Dependencies**: Web, Actuator, RestClient, Artemis JMS, DevTools, Test

## Project Structure

```
src/main/java/dev/danvega/sb4/
├── Application.java                    # Main Spring Boot application
├── api_versioning/                     # Content negotiation API versioning
├── bean_registration/                  # Programmatic bean registration
├── http_interface_clients/             # Declarative HTTP clients
├── jms_client/                        # JMS messaging with Artemis
├── null_safety/                       # JSpecify null safety annotations
└── resilience/                        # Core resilience features
```

## Key Spring Boot 4 Features

- **Zero external dependencies for resilience** - @Retryable and @ConcurrencyLimit built into Spring Framework 7
- **Enhanced HTTP clients** - @HttpExchange declarative interfaces
- **Improved null safety** - JSpecify annotations with @NullMarked packages
- **Flexible bean registration** - Programmatic registration with ImportBeanDefinitionRegistrar
- **Advanced API versioning** - Content negotiation with custom media types
- **Enterprise messaging** - JMS client with embedded Apache Artemis

## Development Notes

This project uses Spring Boot DevTools for hot reloading and includes Actuator endpoints for monitoring. Each feature demonstration includes comprehensive tests and follows real-world usage patterns.

For detailed implementation guidance, see `CLAUDE.md`.