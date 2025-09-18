package dev.danvega.sb4.http_interface_clients;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("TodoController HTTP Interface Tests")
class TodoControllerTest {

    private RestTestClient client;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        client = RestTestClient.bindToApplicationContext(context).build();
    }

    @Nested
    @DisplayName("GET /api/todos/ - Get All Todos")
    class GetAllTodosTests {

        @Test
        @DisplayName("Returns all todos with correct JSON structure")
        @SuppressWarnings("all")
        void getAllTodos_returnsAllTodosWithCorrectStructure() {
            client.get()
                    .uri("/api/todos/")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$.length()").isEqualTo(200)
                    .jsonPath("$[0].id").isEqualTo(1)
                    .jsonPath("$[0].userId").isEqualTo(1)
                    .jsonPath("$[0].title").isEqualTo("delectus aut autem")
                    .jsonPath("$[0].completed").isEqualTo(false);
        }
    }

    @Nested
    @DisplayName("GET /api/todos/{id} - Get Todo By ID")
    class GetTodoByIdTests {

        @Test
        @DisplayName("Returns specific todo when valid ID provided")
        void getTodoById_returnsSpecificTodoWhenValidId() {
            client.get()
                    .uri("/api/todos/1")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.id").isEqualTo(1)
                    .jsonPath("$.userId").isEqualTo(1)
                    .jsonPath("$.title").isEqualTo("delectus aut autem")
                    .jsonPath("$.completed").isEqualTo(false);
        }

        @Test
        @DisplayName("Returns specific todo by ID correctly")
        void getTodoById_returnsSpecificTodoById() {
            client.get()
                    .uri("/api/todos/2")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.id").isEqualTo(2)
                    .jsonPath("$.userId").isEqualTo(1)
                    .jsonPath("$.title").isEqualTo("quis ut nam facilis et officia qui")
                    .jsonPath("$.completed").isEqualTo(false);
        }
    }

    @Nested
    @DisplayName("GET /api/todos/user/{userId} - Get Todos By User ID")
    class GetTodosByUserIdTests {

        @Test
        @DisplayName("Returns todos filtered by user ID")
        void getTodosByUserId_returnsTodosFilteredByUserId() {
            client.get()
                    .uri("/api/todos/user/1")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$.length()").isEqualTo(20)
                    .jsonPath("$[0].userId").isEqualTo(1)
                    .jsonPath("$[1].userId").isEqualTo(1)
                    .jsonPath("$[0].id").isEqualTo(1)
                    .jsonPath("$[1].id").isEqualTo(2);
        }

        @Test
        @DisplayName("Returns empty array when user has no todos")
        void getTodosByUserId_returnsEmptyArrayWhenUserHasNoTodos() {
            client.get()
                    .uri("/api/todos/user/999")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$.length()").isEqualTo(0);
        }

        @Test
        @DisplayName("Returns todos for different user ID")
        void getTodosByUserId_returnsTodosForDifferentUserId() {
            client.get()
                    .uri("/api/todos/user/2")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$.length()").isEqualTo(20)
                    .jsonPath("$[0].userId").isEqualTo(2)
                    .jsonPath("$[1].userId").isEqualTo(2)
                    .jsonPath("$[0].id").isEqualTo(21)
                    .jsonPath("$[1].id").isEqualTo(22);
        }
    }
}