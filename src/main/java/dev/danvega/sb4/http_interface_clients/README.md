# HTTP Interface Clients Demo

This demo showcases Spring Framework 7's HTTP Interface Clients feature with the new `@ImportHttpServices` annotation in Spring Boot 4.0.0-M2.

## What's New in Spring Boot 4.0.0-M2

HTTP Interface Clients can now be registered with **zero boilerplate** using the `@ImportHttpServices` annotation:

```java
@Configuration(proxyBeanMethods = false)
@ImportHttpServices(TodoService.class)
public class HttpClientConfig {
    // That's it! Spring Boot automatically handles the rest
}
```

## HTTP Interface Definition

Define your HTTP client as a simple interface with declarative annotations:

```java
@HttpExchange(url = "https://jsonplaceholder.typicode.com", accept = "application/json")
public interface TodoService {
    @GetExchange("/todos")
    List<Todo> getAllTodos();

    @GetExchange("/todos/{id}")
    Todo getTodoById(@PathVariable Long id);

    // Full CRUD operations supported...
}
```

## Key Benefits

1. **Zero Configuration**: No manual bean setup required
2. **Type Safe**: Compile-time checking with Java records
3. **Declarative**: Clean interface definitions
4. **Performance Optimized**: `proxyBeanMethods = false` reduces overhead
5. **Easy Testing**: Interface can be easily mocked

## Running the Demo

```bash
./mvnw spring-boot:run
```

Visit: http://localhost:8080/api/todos/

## Before vs After

### Before (Manual Configuration)

```java
@Bean
public TodoService todoService(RestClient.Builder restClientBuilder) {
    var restClient = restClientBuilder
            .baseUrl("https://jsonplaceholder.typicode.com")
            .build();

    var adapter = RestClientAdapter.create(restClient);
    var factory = HttpServiceProxyFactory.builderFor(adapter).build();
    return factory.createClient(TodoService.class);
}
```

### After (Spring Boot 4.0.0-M2)
```java
@Configuration(proxyBeanMethods = false)
@ImportHttpServices(TodoService.class)
public class HttpClientConfig {
    // That's it! 🎉
}
```

The `@ImportHttpServices` annotation represents a **significant reduction in configuration complexity** while maintaining all the same functionality.