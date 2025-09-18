package dev.danvega.sb4.http_interface_clients;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TodoServiceTest {

    private TodoService todoService;

    @BeforeEach
    void setUp() {
        RestClient restClient = RestClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        todoService = factory.createClient(TodoService.class);
    }

    @Test
    void getAllTodos_shouldReturnListOfTodos() {
        List<Todo> todos = todoService.getAllTodos();

        assertNotNull(todos);
        assertFalse(todos.isEmpty());
        assertTrue(todos.size() >= 1);
    }

    @Test
    void getTodoById_shouldReturnSingleTodo() {
        Todo todo = todoService.getTodoById(1L);

        assertNotNull(todo);
        assertEquals(1L, todo.id());
        assertEquals(1L, todo.userId());
        assertEquals("delectus aut autem", todo.title());
        assertFalse(todo.completed());
    }

    @Test
    void getTodosByUserId_shouldReturnTodosForUser() {
        List<Todo> todos = todoService.getTodosByUserId(1L);

        assertNotNull(todos);
        assertFalse(todos.isEmpty());
        todos.forEach(todo -> assertEquals(1L, todo.userId()));
    }

    @Test
    void createTodo_shouldReturnCreatedTodo() {
        Todo newTodo = new Todo(null, 1L, "New Todo", false);

        Todo createdTodo = todoService.createTodo(newTodo);

        assertNotNull(createdTodo);
        assertNotNull(createdTodo.id());
        assertEquals("New Todo", createdTodo.title());
        assertEquals(1L, createdTodo.userId());
        assertFalse(createdTodo.completed());
    }

    @Test
    void updateTodo_shouldReturnUpdatedTodo() {
        Todo updatedTodo = new Todo(1L, 1L, "Updated Todo", true);

        Todo result = todoService.updateTodo(1L, updatedTodo);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Updated Todo", result.title());
        assertTrue(result.completed());
    }

    @Test
    void deleteTodo_shouldCallDeleteEndpoint() {
        assertDoesNotThrow(() -> todoService.deleteTodo(1L));
    }
}