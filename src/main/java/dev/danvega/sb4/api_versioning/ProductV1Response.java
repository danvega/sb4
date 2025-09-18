package dev.danvega.sb4.api_versioning;

import java.math.BigDecimal;

public record ProductV1Response(
        Long id,
        String name,
        String description,
        BigDecimal price,
        boolean inStock
) {
    public static ProductV1Response fromProduct(Product product) {
        return new ProductV1Response(
                product.id(),
                product.name(),
                product.description(),
                product.price(),
                product.inStock()
        );
    }
}