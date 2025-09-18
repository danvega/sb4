package dev.danvega.sb4.api_versioning;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductV2Response(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String category,
        boolean inStock,
        LocalDateTime createdAt,
        String manufacturer,
        String sku,
        Integer stockQuantity
) {
    public static ProductV2Response fromProduct(Product product) {
        return new ProductV2Response(
                product.id(),
                product.name(),
                product.description(),
                product.price(),
                product.category(),
                product.inStock(),
                product.createdAt(),
                product.manufacturer(),
                product.sku(),
                product.stockQuantity()
        );
    }
}