# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 4.0.0-M2 demo project showcasing new features in Spring Boot 4. The project uses Java 24 and Maven for build management. It demonstrates various Spring Boot 4 features through practical implementations with complete working examples and tests.

## Build and Development Commands

### Building the Application
```bash
./mvnw clean compile
./mvnw clean package
```

### Running the Application
```bash
./mvnw spring-boot:run
```

### Running Tests
```bash
./mvnw test
./mvnw verify
```

### Running a Single Test
```bash
./mvnw test -Dtest=ApplicationTests
./mvnw test -Dtest=ApplicationTests#contextLoads
```

## Project Structure

- **Main Application**: `src/main/java/dev/danvega/sb4/Application.java` - Standard Spring Boot main class
- **Configuration**: `src/main/resources/application.properties` - Application configuration
- **Tests**: `src/test/java/dev/danvega/sb4/ApplicationTests.java` - Basic Spring Boot context test

### Implemented Tutorial Packages

- **`null_safety/`** - JSpecify null safety annotations with @NullMarked package annotation
- **`http_interface_clients/`** - HTTP Interface Clients using @HttpExchange annotations
- **`bean_registration/`** - Programmatic Bean Registration with MessageServiceRegistrar
- **`api_versioning/`** - API versioning with V1/V2 response models
- **`jms_client/`** - JMS Client implementation using Apache Artemis
- **`resilience/`** - Core resilience features with @Retryable and @ConcurrencyLimit annotations

## Technology Stack

- **Java Version**: 24
- **Spring Boot Version**: 4.0.0-M2 (milestone release)
- **Build Tool**: Maven with wrapper (`./mvnw`)
- **Included Dependencies**:
  - spring-boot-starter-web (REST controllers)
  - spring-boot-starter-actuator (monitoring and management)
  - spring-boot-restclient (HTTP Interface Clients)
  - spring-boot-starter-artemis (JMS messaging)
  - artemis-jms-server (embedded JMS server)
  - spring-boot-devtools (development hot reload)
  - spring-boot-starter-test (testing framework)

## Implemented Features

### 1. JSpecify Null Safety (`null_safety/`)
Demonstrates JSpecify annotations for null safety with package-level @NullMarked annotation.
- **Endpoints**: `/api/users` - User CRUD operations with null safety
- **Key Files**: `package-info.java`, `UserController.java`, `UserService.java`

### 2. HTTP Interface Clients (`http_interface_clients/`)
Shows the new @HttpExchange declarative HTTP clients replacing traditional RestTemplate/WebClient.
- **Endpoints**: `/api/todos` - Todo operations using interface clients
- **Key Files**: `TodoService.java` (interface), `TodoController.java`
- **External API**: JSONPlaceholder for demo data

### 3. Programmatic Bean Registration (`bean_registration/`)
Demonstrates programmatic bean registration using ImportBeanDefinitionRegistrar.
- **Endpoints**: `/api/messages` - Message service with different implementations
- **Key Files**: `MessageServiceRegistrar.java`, `ModernConfig.java`
- **Services**: Email and SMS message services

### 4. API Versioning (`api_versioning/`)
Shows content negotiation-based API versioning with different response models.
- **Endpoints**: `/api/products` - Products with V1/V2 response formats
- **Key Files**: `ProductController.java`, `ProductV1Response.java`, `ProductV2Response.java`
- **Headers**: `Accept: application/vnd.api.v1+json` or `application/vnd.api.v2+json`

### 5. JMS Client (`jms_client/`)
Implements JMS messaging using Apache Artemis with producer and consumer.
- **Endpoints**: `/api/notifications` - Send and receive notifications
- **Key Files**: `NotificationProducer.java`, `NotificationConsumer.java`
- **Queue**: `notification.queue`

### 6. Core Resilience Features (`resilience/`)
Demonstrates Spring Framework 7's built-in resilience features without external dependencies.
- **Endpoints**: `/api/resilience/**` - Simple database operations with resilience patterns
- **Key Files**: `DatabaseService.java`, `ResilienceDemoController.java`, `ResilienceConfig.java`
- **Features**: @Retryable with exponential backoff, @ConcurrencyLimit for resource control

## Development Notes

This is a milestone release project (4.0.0-M2) with fully implemented working examples of Spring Boot 4 features. Each tutorial includes comprehensive tests and demonstrates real-world usage patterns. When adding new demos, follow the existing package structure under `dev.danvega.sb4`.

The project uses Spring Boot DevTools for hot reloading during development, and includes Actuator endpoints for monitoring and management features.