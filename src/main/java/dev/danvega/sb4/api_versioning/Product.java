package dev.danvega.sb4.api_versioning;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Product(
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
}