package dev.danvega.sb4.http_interface_clients;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class TraditionalTodoService {

    private final RestClient restClient;

    public TraditionalTodoService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();
    }

    public List<Todo> getAllTodos() {
        return restClient
                .get()
                .uri("/todos")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Todo>>() {});
    }

    public Todo getTodoById(Long id) {
        return restClient
                .get()
                .uri("/todos/{id}", id)
                .retrieve()
                .body(Todo.class);
    }

    public List<Todo> getTodosByUserId(Long userId) {
        return restClient
                .get()
                .uri("/todos?userId={userId}", userId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Todo>>() {});
    }

    public Todo createTodo(Todo todo) {
        return restClient
                .post()
                .uri("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(todo)
                .retrieve()
                .body(Todo.class);
    }

    public Todo updateTodo(Long id, Todo todo) {
        return restClient
                .put()
                .uri("/todos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(todo)
                .retrieve()
                .body(Todo.class);
    }

    public void deleteTodo(Long id) {
        restClient
                .delete()
                .uri("/todos/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}