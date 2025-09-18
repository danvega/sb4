# What's new in Spring Boot 4

This is a collection of working code examples demonstrating new features in Spring Boot 4.0.0-M2. Each tutorial includes fully implemented examples with comprehensive tests.

## Implemented Tutorials

### ✅ JSpecify Null Safety
- **Package**: `null_safety/`
- **Endpoint**: `GET/POST /api/users`
- **Features**: Package-level @NullMarked annotations, null-safe operations
- **Try it**: `curl http://localhost:8080/api/users`

### ✅ HTTP Interface Clients
- **Package**: `http_interface_clients/`
- **Endpoint**: `GET /api/todos`
- **Features**: Declarative HTTP clients with @HttpExchange, replacing RestTemplate
- **Try it**: `curl http://localhost:8080/api/todos`

### ✅ Programmatic Bean Registration
- **Package**: `bean_registration/`
- **Endpoint**: `GET /api/messages`
- **Features**: Dynamic bean registration using ImportBeanDefinitionRegistrar
- **Try it**: `curl http://localhost:8080/api/messages`

### ✅ API Versioning
- **Package**: `api_versioning/`
- **Endpoint**: `GET /api/products`
- **Features**: Content negotiation-based versioning with different response models
- **Try it**:
  - V1: `curl -H "Accept: application/vnd.api.v1+json" http://localhost:8080/api/products`
  - V2: `curl -H "Accept: application/vnd.api.v2+json" http://localhost:8080/api/products`

### ✅ JMS Client
- **Package**: `jms_client/`
- **Endpoint**: `POST /api/notifications`
- **Features**: Apache Artemis integration with producer/consumer pattern
- **Try it**: `curl -X POST -H "Content-Type: application/json" -d '{"message":"Hello JMS"}' http://localhost:8080/api/notifications`

## Technology Stack

- **Spring Boot**: 4.0.0-M2 (Milestone Release)
- **Java**: 24
- **Build Tool**: Maven with wrapper
- **Message Broker**: Apache Artemis (embedded)
- **Testing**: Spring Boot Test with comprehensive test coverage

## Getting Started

```bash
# Run the application
./mvnw spring-boot:run

# Run tests
./mvnw test

# Build the project
./mvnw clean package
```

## Project Structure

Each tutorial is organized in its own package under `src/main/java/dev/danvega/sb4/`:
- `null_safety/` - JSpecify demonstrations
- `http_interface_clients/` - HTTP client interfaces
- `bean_registration/` - Programmatic bean configuration
- `api_versioning/` - API versioning patterns
- `jms_client/` - JMS messaging examples

## Future Tutorials

Potential additions for future Spring Boot 4 features:
- Core Resiliency Features
- Additional JDK 24/25 integrations 