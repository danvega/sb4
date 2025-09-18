package dev.danvega.sb4.api_versioning;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final List<Product> products;

    public ProductService() {
        this.products = initializeProducts();
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public Optional<Product> getProductById(Long id) {
        return products.stream()
                .filter(product -> product.id().equals(id))
                .findFirst();
    }

    private List<Product> initializeProducts() {
        return List.of(
                new Product(
                        1L,
                        "MacBook Pro",
                        "Apple MacBook Pro 14-inch with M2 chip",
                        new BigDecimal("1999.99"),
                        "Electronics",
                        true,
                        LocalDateTime.now().minusDays(30),
                        "Apple",
                        "MBP-M2-14",
                        15
                ),
                new Product(
                        2L,
                        "Gaming Chair",
                        "Ergonomic gaming chair with lumbar support",
                        new BigDecimal("299.99"),
                        "Furniture",
                        true,
                        LocalDateTime.now().minusDays(15),
                        "DXRacer",
                        "GC-001",
                        8
                ),
                new Product(
                        3L,
                        "Coffee Maker",
                        "Programmable coffee maker with thermal carafe",
                        new BigDecimal("89.99"),
                        "Kitchen",
                        false,
                        LocalDateTime.now().minusDays(60),
                        "Cuisinart",
                        "CM-10",
                        0
                ),
                new Product(
                        4L,
                        "Wireless Headphones",
                        "Noise-cancelling wireless headphones",
                        new BigDecimal("199.99"),
                        "Electronics",
                        true,
                        LocalDateTime.now().minusDays(7),
                        "Sony",
                        "WH-1000XM4",
                        25
                ),
                new Product(
                        5L,
                        "Running Shoes",
                        "Lightweight running shoes for daily training",
                        new BigDecimal("129.99"),
                        "Sports",
                        true,
                        LocalDateTime.now().minusDays(3),
                        "Nike",
                        "NIKE-RUN-001",
                        12
                )
        );
    }
}