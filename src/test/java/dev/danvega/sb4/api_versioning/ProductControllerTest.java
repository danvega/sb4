package dev.danvega.sb4.api_versioning;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("ProductController API Versioning Tests")
//noinspection all
class ProductControllerTest {

    private RestTestClient client;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        client = RestTestClient.bindToApplicationContext(context).build();
    }

    @Nested
    @DisplayName("Header-Based Versioning Tests")
    class HeaderBasedVersioningTests {

        @Nested
        @DisplayName("API Version 1.0 (Deprecated)")
        class ApiVersion1Tests {

            @Test
            @DisplayName("GET /api/products with Api-Version: 1.0 returns V1 format with deprecation headers")
            void getAllProducts_withApiVersion1_returnsV1FormatWithDeprecationHeaders() {
                client.get()
                        .uri("/api/products")
                        .header("Api-Version", "1.0")
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isOk()
                        .expectHeader().contentType(MediaType.APPLICATION_JSON)
                        .expectHeader().valueEquals("Deprecation", "true")
                        .expectHeader().exists("Sunset")
                        .expectHeader().valueEquals("Link", "</api/products>; rel=\"successor-version\"; version=\"2.0\"")
                        .expectBody()
                        .jsonPath("$").isArray()
                        .jsonPath("$.length()").isEqualTo(5)
                        .jsonPath("$[0].id").isEqualTo(1)
                        .jsonPath("$[0].name").isEqualTo("MacBook Pro")
                        .jsonPath("$[0].description").isEqualTo("Apple MacBook Pro 14-inch with M2 chip")
                        .jsonPath("$[0].price").isEqualTo(1999.99)
                        .jsonPath("$[0].inStock").isEqualTo(true)
                        // Verify V1 format - these fields should NOT exist
                        .jsonPath("$[0].category").doesNotExist()
                        .jsonPath("$[0].manufacturer").doesNotExist()
                        .jsonPath("$[0].sku").doesNotExist()
                        .jsonPath("$[0].stockQuantity").doesNotExist()
                        .jsonPath("$[0].createdAt").doesNotExist();
            }

            @Test
            @DisplayName("GET /api/products/{id} with Api-Version: 1.0 returns single product V1 format")
            void getProductById_withApiVersion1_returnsSingleProductV1Format() {
                client.get()
                        .uri("/api/products/1")
                        .header("Api-Version", "1.0")
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isOk()
                        .expectHeader().contentType(MediaType.APPLICATION_JSON)
                        .expectHeader().valueEquals("Deprecation", "true")
                        .expectHeader().exists("Sunset")
                        .expectHeader().valueEquals("Link", "</api/products>; rel=\"successor-version\"; version=\"2.0\"")
                        .expectBody()
                        .jsonPath("$.id").isEqualTo(1)
                        .jsonPath("$.name").isEqualTo("MacBook Pro")
                        .jsonPath("$.description").isEqualTo("Apple MacBook Pro 14-inch with M2 chip")
                        .jsonPath("$.price").isEqualTo(1999.99)
                        .jsonPath("$.inStock").isEqualTo(true)
                        // Verify V1 format - these fields should NOT exist
                        .jsonPath("$.category").doesNotExist()
                        .jsonPath("$.manufacturer").doesNotExist()
                        .jsonPath("$.sku").doesNotExist()
                        .jsonPath("$.stockQuantity").doesNotExist()
                        .jsonPath("$.createdAt").doesNotExist();
            }

            @Test
            @DisplayName("GET /api/products/{id} with Api-Version: 1.0 returns 404 for non-existent product")
            void getProductById_withApiVersion1_returns404ForNonExistentProduct() {
                client.get()
                        .uri("/api/products/999")
                        .header("Api-Version", "1.0")
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isNotFound();
            }
        }

        @Nested
        @DisplayName("API Version 2.0 (Current)")
        class ApiVersion2Tests {

            @Test
            @DisplayName("GET /api/products with Api-Version: 2.0 returns V2 format without deprecation headers")
            void getAllProducts_withApiVersion2_returnsV2FormatWithoutDeprecationHeaders() {
                client.get()
                        .uri("/api/products")
                        .header("Api-Version", "2.0")
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isOk()
                        .expectHeader().contentType(MediaType.APPLICATION_JSON)
                        .expectHeader().doesNotExist("Deprecation")
                        .expectHeader().doesNotExist("Sunset")
                        .expectHeader().doesNotExist("Link")
                        .expectBody()
                        .jsonPath("$").isArray()
                        .jsonPath("$.length()").isEqualTo(5)
                        .jsonPath("$[0].id").isEqualTo(1)
                        .jsonPath("$[0].name").isEqualTo("MacBook Pro")
                        .jsonPath("$[0].description").isEqualTo("Apple MacBook Pro 14-inch with M2 chip")
                        .jsonPath("$[0].price").isEqualTo(1999.99)
                        .jsonPath("$[0].inStock").isEqualTo(true)
                        // Verify V2 format - these fields SHOULD exist
                        .jsonPath("$[0].category").isEqualTo("Electronics")
                        .jsonPath("$[0].manufacturer").isEqualTo("Apple")
                        .jsonPath("$[0].sku").isEqualTo("MBP-M2-14")
                        .jsonPath("$[0].stockQuantity").isEqualTo(15)
                        .jsonPath("$[0].createdAt").exists();
            }

            @Test
            @DisplayName("GET /api/products/{id} with Api-Version: 2.0 returns single product V2 format")
            void getProductById_withApiVersion2_returnsSingleProductV2Format() {
                client.get()
                        .uri("/api/products/1")
                        .header("Api-Version", "2.0")
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isOk()
                        .expectHeader().contentType(MediaType.APPLICATION_JSON)
                        .expectHeader().doesNotExist("Deprecation")
                        .expectHeader().doesNotExist("Sunset")
                        .expectHeader().doesNotExist("Link")
                        .expectBody()
                        .jsonPath("$.id").isEqualTo(1)
                        .jsonPath("$.name").isEqualTo("MacBook Pro")
                        .jsonPath("$.description").isEqualTo("Apple MacBook Pro 14-inch with M2 chip")
                        .jsonPath("$.price").isEqualTo(1999.99)
                        .jsonPath("$.inStock").isEqualTo(true)
                        // Verify V2 format - these fields SHOULD exist
                        .jsonPath("$.category").isEqualTo("Electronics")
                        .jsonPath("$.manufacturer").isEqualTo("Apple")
                        .jsonPath("$.sku").isEqualTo("MBP-M2-14")
                        .jsonPath("$.stockQuantity").isEqualTo(15)
                        .jsonPath("$.createdAt").exists();
            }

            @Test
            @DisplayName("GET /api/products/{id} with Api-Version: 2.0 returns 404 for non-existent product")
            void getProductById_withApiVersion2_returns404ForNonExistentProduct() {
                client.get()
                        .uri("/api/products/999")
                        .header("Api-Version", "2.0")
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isNotFound();
            }
        }
    }

    @Nested
    @DisplayName("Path-Based Versioning Tests")
    class PathBasedVersioningTests {

        @Nested
        @DisplayName("Version 1 Path Endpoints (/v1/)")
        class Version1PathTests {

            @Test
            @DisplayName("GET /api/v1/products returns V1 format with deprecation headers")
            void getAllProducts_v1Path_returnsV1FormatWithDeprecationHeaders() {
                client.get()
                        .uri("/api/v1/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isOk()
                        .expectHeader().contentType(MediaType.APPLICATION_JSON)
                        .expectHeader().valueEquals("Deprecation", "true")
                        .expectHeader().exists("Sunset")
                        .expectHeader().valueEquals("Link", "</api/products>; rel=\"successor-version\"; version=\"2.0\"")
                        .expectBody()
                        .jsonPath("$").isArray()
                        .jsonPath("$.length()").isEqualTo(5)
                        .jsonPath("$[0].id").isEqualTo(1)
                        .jsonPath("$[0].name").isEqualTo("MacBook Pro")
                        // Verify V1 format - enhanced fields should NOT exist
                        .jsonPath("$[0].category").doesNotExist()
                        .jsonPath("$[0].manufacturer").doesNotExist()
                        .jsonPath("$[0].sku").doesNotExist()
                        .jsonPath("$[0].stockQuantity").doesNotExist()
                        .jsonPath("$[0].createdAt").doesNotExist();
            }

            @Test
            @DisplayName("GET /api/v1/products/{id} returns single product V1 format with deprecation headers")
            void getProductById_v1Path_returnsSingleProductV1Format() {
                client.get()
                        .uri("/api/v1/products/2")
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isOk()
                        .expectHeader().contentType(MediaType.APPLICATION_JSON)
                        .expectHeader().valueEquals("Deprecation", "true")
                        .expectHeader().exists("Sunset")
                        .expectHeader().valueEquals("Link", "</api/products>; rel=\"successor-version\"; version=\"2.0\"")
                        .expectBody()
                        .jsonPath("$.id").isEqualTo(2)
                        .jsonPath("$.name").isEqualTo("Gaming Chair")
                        .jsonPath("$.description").isEqualTo("Ergonomic gaming chair with lumbar support")
                        .jsonPath("$.price").isEqualTo(299.99)
                        .jsonPath("$.inStock").isEqualTo(true)
                        // Verify V1 format - enhanced fields should NOT exist
                        .jsonPath("$.category").doesNotExist()
                        .jsonPath("$.manufacturer").doesNotExist()
                        .jsonPath("$.sku").doesNotExist()
                        .jsonPath("$.stockQuantity").doesNotExist()
                        .jsonPath("$.createdAt").doesNotExist();
            }

            @Test
            @DisplayName("GET /api/v1/products/{id} returns 404 for non-existent product")
            void getProductById_v1Path_returns404ForNonExistentProduct() {
                client.get()
                        .uri("/api/v1/products/999")
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isNotFound();
            }
        }

        @Nested
        @DisplayName("Version 2 Path Endpoints (/v2/)")
        class Version2PathTests {

            @Test
            @DisplayName("GET /api/v2/products returns V2 format without deprecation headers")
            void getAllProducts_v2Path_returnsV2FormatWithoutDeprecationHeaders() {
                client.get()
                        .uri("/api/v2/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isOk()
                        .expectHeader().contentType(MediaType.APPLICATION_JSON)
                        .expectHeader().doesNotExist("Deprecation")
                        .expectHeader().doesNotExist("Sunset")
                        .expectHeader().doesNotExist("Link")
                        .expectBody()
                        .jsonPath("$").isArray()
                        .jsonPath("$.length()").isEqualTo(5)
                        .jsonPath("$[0].id").isEqualTo(1)
                        .jsonPath("$[0].name").isEqualTo("MacBook Pro")
                        // Verify V2 format - enhanced fields SHOULD exist
                        .jsonPath("$[0].category").isEqualTo("Electronics")
                        .jsonPath("$[0].manufacturer").isEqualTo("Apple")
                        .jsonPath("$[0].sku").isEqualTo("MBP-M2-14")
                        .jsonPath("$[0].stockQuantity").isEqualTo(15)
                        .jsonPath("$[0].createdAt").exists();
            }

            @Test
            @DisplayName("GET /api/v2/products/{id} returns single product V2 format without deprecation headers")
            void getProductById_v2Path_returnsSingleProductV2Format() {
                client.get()
                        .uri("/api/v2/products/3")
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isOk()
                        .expectHeader().contentType(MediaType.APPLICATION_JSON)
                        .expectHeader().doesNotExist("Deprecation")
                        .expectHeader().doesNotExist("Sunset")
                        .expectHeader().doesNotExist("Link")
                        .expectBody()
                        .jsonPath("$.id").isEqualTo(3)
                        .jsonPath("$.name").isEqualTo("Coffee Maker")
                        .jsonPath("$.description").isEqualTo("Programmable coffee maker with thermal carafe")
                        .jsonPath("$.price").isEqualTo(89.99)
                        .jsonPath("$.inStock").isEqualTo(false)
                        // Verify V2 format - enhanced fields SHOULD exist
                        .jsonPath("$.category").isEqualTo("Kitchen")
                        .jsonPath("$.manufacturer").isEqualTo("Cuisinart")
                        .jsonPath("$.sku").isEqualTo("CM-10")
                        .jsonPath("$.stockQuantity").isEqualTo(0)
                        .jsonPath("$.createdAt").exists();
            }

            @Test
            @DisplayName("GET /api/v2/products/{id} returns 404 for non-existent product")
            void getProductById_v2Path_returns404ForNonExistentProduct() {
                client.get()
                        .uri("/api/v2/products/999")
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isNotFound();
            }
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
                    .uri("/api/products")
                    .header("Api-Version", "1.0")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().valueEquals("Deprecation", "true")
                    .expectHeader().valueEquals("Sunset", expectedSunsetDate)
                    .expectHeader().valueEquals("Link", "</api/products>; rel=\"successor-version\"; version=\"2.0\"");
        }

        @Test
        @DisplayName("V2.0 endpoints do not include any deprecation headers")
        void v2Endpoints_doNotIncludeDeprecationHeaders() {
            client.get()
                    .uri("/api/products")
                    .header("Api-Version", "2.0")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().doesNotExist("Deprecation")
                    .expectHeader().doesNotExist("Sunset")
                    .expectHeader().doesNotExist("Link");
        }
    }

    @Nested
    @DisplayName("Product Data Validation")
    class ProductDataValidationTests {

        @Test
        @DisplayName("All products contain expected sample data")
        void getAllProducts_containsExpectedSampleData() {
            client.get()
                    .uri("/api/v2/products")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$.length()").isEqualTo(5)
                    // Verify specific products exist
                    .jsonPath("$[?(@.name == 'MacBook Pro')]").exists()
                    .jsonPath("$[?(@.name == 'Gaming Chair')]").exists()
                    .jsonPath("$[?(@.name == 'Coffee Maker')]").exists()
                    .jsonPath("$[?(@.name == 'Wireless Headphones')]").exists()
                    .jsonPath("$[?(@.name == 'Running Shoes')]").exists()
                    // Verify categories
                    .jsonPath("$[?(@.category == 'Electronics')]").exists()
                    .jsonPath("$[?(@.category == 'Furniture')]").exists()
                    .jsonPath("$[?(@.category == 'Kitchen')]").exists()
                    .jsonPath("$[?(@.category == 'Sports')]").exists();
        }

        @Test
        @DisplayName("Coffee Maker product shows as out of stock in both versions")
        void coffeeMakerProduct_showsAsOutOfStockInBothVersions() {
            // Test V1 format
            client.get()
                    .uri("/api/products/3")
                    .header("Api-Version", "1.0")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.inStock").isEqualTo(false);

            // Test V2 format
            client.get()
                    .uri("/api/products/3")
                    .header("Api-Version", "2.0")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.inStock").isEqualTo(false)
                    .jsonPath("$.stockQuantity").isEqualTo(0);
        }
    }
}