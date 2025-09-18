package dev.danvega.sb4.api_versioning;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/products", headers = "Api-Version=1.0")
    public ResponseEntity<List<ProductV1Response>> getAllProductsV1() {
        List<ProductV1Response> products = productService.getAllProducts()
                .stream()
                .map(ProductV1Response::fromProduct)
                .toList();

        HttpHeaders headers = createDeprecationHeaders();
        return ResponseEntity.ok().headers(headers).body(products);
    }

    @GetMapping(value = "/products", headers = "Api-Version=2.0")
    public List<ProductV2Response> getAllProductsV2() {
        return productService.getAllProducts()
                .stream()
                .map(ProductV2Response::fromProduct)
                .toList();
    }

    @GetMapping(value = "/products/{id}", headers = "Api-Version=1.0")
    public ResponseEntity<ProductV1Response> getProductByIdV1(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> {
                    HttpHeaders headers = createDeprecationHeaders();
                    return ResponseEntity.ok().headers(headers).body(ProductV1Response.fromProduct(product));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/products/{id}", headers = "Api-Version=2.0")
    public ResponseEntity<ProductV2Response> getProductByIdV2(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok(ProductV2Response.fromProduct(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/v1/products")
    public ResponseEntity<List<ProductV1Response>> getAllProductsV1PathBased() {
        List<ProductV1Response> products = productService.getAllProducts()
                .stream()
                .map(ProductV1Response::fromProduct)
                .toList();

        HttpHeaders headers = createDeprecationHeaders();
        return ResponseEntity.ok().headers(headers).body(products);
    }

    @GetMapping("/v2/products")
    public List<ProductV2Response> getAllProductsV2PathBased() {
        return productService.getAllProducts()
                .stream()
                .map(ProductV2Response::fromProduct)
                .toList();
    }

    @GetMapping("/v1/products/{id}")
    public ResponseEntity<ProductV1Response> getProductByIdV1PathBased(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> {
                    HttpHeaders headers = createDeprecationHeaders();
                    return ResponseEntity.ok().headers(headers).body(ProductV1Response.fromProduct(product));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/v2/products/{id}")
    public ResponseEntity<ProductV2Response> getProductByIdV2PathBased(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok(ProductV2Response.fromProduct(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    private HttpHeaders createDeprecationHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Deprecation", "true");
        headers.add("Sunset", LocalDate.now().plusMonths(6).toString());
        headers.add("Link", "</api/products>; rel=\"successor-version\"; version=\"2.0\"");
        return headers;
    }
}