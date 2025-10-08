package dev.danvega.sb4.api_versioning;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("ApiVersioningUserController API Versioning Tests")
class ApiVersioningUserControllerTest {

    private RestTestClient client;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        client = RestTestClient.bindToApplicationContext(context).build();
    }

    @Nested
    @DisplayName("API Version 1.0 (Deprecated)")
    class ApiVersion1Tests {

        @Test
        @DisplayName("GET /api/users with version 1.0 returns V1 format with combined name and deprecation headers")
        void getAllUsers_withApiVersion1_returnsV1FormatWithDeprecationHeaders() {
            client.get()
                    .uri("/api/users")
                    .accept(MediaType.parseMediaType("application/json;version=1.0"))
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.parseMediaType("application/json;version=1.0"))
                    .expectHeader().valueEquals("Deprecation", "true")
                    .expectHeader().exists("Sunset")
                    .expectHeader().valueEquals("Link", "</api/users>; rel=\"successor-version\"; version=\"2.0\"")
                    .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$.length()").isEqualTo(5)
                    .jsonPath("$[0].id").isEqualTo(1)
                    .jsonPath("$[0].name").isEqualTo("Dan Vega")
                    .jsonPath("$[0].email").isEqualTo("dan@example.com")
                    // Verify V1 format - firstName/lastName should NOT exist
                    .jsonPath("$[0].firstName").doesNotExist()
                    .jsonPath("$[0].lastName").doesNotExist();
        }

        @Test
        @DisplayName("GET /api/users/{id} with version 1.0 returns single user V1 format")
        void getUserById_withApiVersion1_returnsSingleUserV1Format() {
            client.get()
                    .uri("/api/users/1")
                    .accept(MediaType.parseMediaType("application/json;version=1.0"))
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.parseMediaType("application/json;version=1.0"))
                    .expectHeader().valueEquals("Deprecation", "true")
                    .expectHeader().exists("Sunset")
                    .expectHeader().valueEquals("Link", "</api/users>; rel=\"successor-version\"; version=\"2.0\"")
                    .expectBody()
                    .jsonPath("$.id").isEqualTo(1)
                    .jsonPath("$.name").isEqualTo("Dan Vega")
                    .jsonPath("$.email").isEqualTo("dan@example.com")
                    // Verify V1 format - firstName/lastName should NOT exist
                    .jsonPath("$.firstName").doesNotExist()
                    .jsonPath("$.lastName").doesNotExist();
        }

        @Test
        @DisplayName("GET /api/users/{id} with version 1.0 returns 404 for non-existent user")
        void getUserById_withApiVersion1_returns404ForNonExistentUser() {
            client.get()
                    .uri("/api/users/999")
                    .accept(MediaType.parseMediaType("application/json;version=1.0"))
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("API Version 2.0 (Current)")
    class ApiVersion2Tests {

        @Test
        @DisplayName("GET /api/users with version 2.0 returns V2 format with separate name fields without deprecation headers")
        void getAllUsers_withApiVersion2_returnsV2FormatWithoutDeprecationHeaders() {
            client.get()
                    .uri("/api/users")
                    .accept(MediaType.parseMediaType("application/json;version=2.0"))
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.parseMediaType("application/json;version=2.0"))
                    .expectHeader().doesNotExist("Deprecation")
                    .expectHeader().doesNotExist("Sunset")
                    .expectHeader().doesNotExist("Link")
                    .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$.length()").isEqualTo(5)
                    .jsonPath("$[0].id").isEqualTo(1)
                    .jsonPath("$[0].firstName").isEqualTo("Dan")
                    .jsonPath("$[0].lastName").isEqualTo("Vega")
                    .jsonPath("$[0].email").isEqualTo("dan@example.com")
                    // Verify V2 format - combined name should NOT exist
                    .jsonPath("$[0].name").doesNotExist();
        }

        @Test
        @DisplayName("GET /api/users/{id} with version 2.0 returns single user V2 format")
        void getUserById_withApiVersion2_returnsSingleUserV2Format() {
            client.get()
                    .uri("/api/users/2")
                    .accept(MediaType.parseMediaType("application/json;version=2.0"))
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.parseMediaType("application/json;version=2.0"))
                    .expectHeader().doesNotExist("Deprecation")
                    .expectHeader().doesNotExist("Sunset")
                    .expectHeader().doesNotExist("Link")
                    .expectBody()
                    .jsonPath("$.id").isEqualTo(2)
                    .jsonPath("$.firstName").isEqualTo("Jane")
                    .jsonPath("$.lastName").isEqualTo("Smith")
                    .jsonPath("$.email").isEqualTo("jane@example.com")
                    // Verify V2 format - combined name should NOT exist
                    .jsonPath("$.name").doesNotExist();
        }

        @Test
        @DisplayName("GET /api/users/{id} with version 2.0 returns 404 for non-existent user")
        void getUserById_withApiVersion2_returns404ForNonExistentUser() {
            client.get()
                    .uri("/api/users/999")
                    .accept(MediaType.parseMediaType("application/json;version=2.0"))
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("Default Version Behavior")
    class DefaultVersionTests {

        @Test
        @DisplayName("GET /api/users without version defaults to version 1.0")
        void getAllUsers_withoutVersion_defaultsToVersion1() {
            client.get()
                    .uri("/api/users")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType("application/json")
                    .expectHeader().valueEquals("Deprecation", "true")
                    .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$[0].id").exists()
                    .jsonPath("$[0].name").exists()
                    .jsonPath("$[0].email").exists()
                    // Should be V1 format
                    .jsonPath("$[0].firstName").doesNotExist()
                    .jsonPath("$[0].lastName").doesNotExist();
        }
    }

    @Nested
    @DisplayName("Deprecation Headers Validation")
    class DeprecationHeadersValidationTests {

        @Test
        @DisplayName("V1.0 deprecation headers contain valid sunset date (6 months from now)")
        void v1DeprecationHeaders_containValidSunsetDate() {
            String expectedSunsetDate = LocalDate.now().plusMonths(6).toString();

            client.get()
                    .uri("/api/users")
                    .accept(MediaType.parseMediaType("application/json;version=1.0"))
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().valueEquals("Deprecation", "true")
                    .expectHeader().valueEquals("Sunset", expectedSunsetDate)
                    .expectHeader().valueEquals("Link", "</api/users>; rel=\"successor-version\"; version=\"2.0\"");
        }

        @Test
        @DisplayName("V2.0 endpoints do not include any deprecation headers")
        void v2Endpoints_doNotIncludeDeprecationHeaders() {
            client.get()
                    .uri("/api/users")
                    .accept(MediaType.parseMediaType("application/json;version=2.0"))
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().doesNotExist("Deprecation")
                    .expectHeader().doesNotExist("Sunset")
                    .expectHeader().doesNotExist("Link");
        }
    }

    @Nested
    @DisplayName("User Data Validation")
    class UserDataValidationTests {

        @Test
        @DisplayName("All users contain expected sample data")
        void getAllUsers_containsExpectedSampleData() {
            client.get()
                    .uri("/api/users")
                    .accept(MediaType.parseMediaType("application/json;version=2.0"))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$.length()").isEqualTo(5)
                    // Verify specific users exist
                    .jsonPath("$[?(@.firstName == 'Dan' && @.lastName == 'Vega')]").exists()
                    .jsonPath("$[?(@.firstName == 'Jane' && @.lastName == 'Smith')]").exists()
                    .jsonPath("$[?(@.firstName == 'John' && @.lastName == 'Doe')]").exists()
                    .jsonPath("$[?(@.firstName == 'Sarah' && @.lastName == 'Johnson')]").exists()
                    .jsonPath("$[?(@.firstName == 'Michael' && @.lastName == 'Brown')]").exists();
        }

        @Test
        @DisplayName("V1 properly combines firstName and lastName into name field")
        void v1Format_properlyCombinesNames() {
            client.get()
                    .uri("/api/users/2")
                    .accept(MediaType.parseMediaType("application/json;version=1.0"))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.name").isEqualTo("Jane Smith");
        }
    }
}
