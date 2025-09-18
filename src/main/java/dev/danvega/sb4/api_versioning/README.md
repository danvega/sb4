# Spring Framework 7 API Versioning Demo

This demo showcases the new API versioning capabilities introduced in Spring Framework 7, demonstrating how to implement multiple versioning strategies for REST APIs while maintaining backward compatibility and providing proper deprecation signals.

## Features Demonstrated

### Core Spring Framework 7 API Versioning Features
- **Header-Based Versioning**: Using `headers = "Api-Version=1.0"` in `@GetMapping` annotations
- **Multiple Versioning Strategies**: Header-based and path-based versioning
- **RFC-Compliant Deprecation**: Deprecation headers (Deprecation, Sunset, Link)
- **Content Negotiation**: Integration with Spring's content negotiation strategy
- **Semantic Version Support**: Foundation for semantic version parsing (when available)

> **Note**: This demo uses Spring Boot 4.0.0-M2 with Spring Framework 7.0.0-M8. The new `version` attribute in `@RequestMapping` annotations is expected in the final release. This implementation demonstrates the versioning concepts using the traditional `headers` attribute approach, which provides the same functionality.

### API Evolution Example
The demo shows the evolution of a Product API from version 1.0 to 2.0:

**Version 1.0 (Basic)**: Basic product information (id, name, description, price, inStock)
**Version 2.0 (Enhanced)**: Extended product information including category, manufacturer, SKU, stock quantity, and creation timestamp

## API Endpoints

### Header-Based Versioning
Use the `Api-Version` header to specify the desired API version:

```bash
# Version 1.0 (deprecated) - Returns basic product info with deprecation headers
curl -H "Api-Version: 1.0" http://localhost:8080/api/products

# Version 2.0 (current) - Returns enhanced product info
curl -H "Api-Version: 2.0" http://localhost:8080/api/products

# Get specific product by ID
curl -H "Api-Version: 1.0" http://localhost:8080/api/products/1
curl -H "Api-Version: 2.0" http://localhost:8080/api/products/1
```

### Path-Based Versioning
Include the version directly in the URL path:

```bash
# Version 1.0 (deprecated) - Returns basic product info with deprecation headers
curl http://localhost:8080/api/v1/products

# Version 2.0 (current) - Returns enhanced product info
curl http://localhost:8080/api/v2/products

# Get specific product by ID
curl http://localhost:8080/api/v1/products/1
curl http://localhost:8080/api/v2/products/1
```

### Media Type Versioning (Future Enhancement)
This demo can be extended to support media type versioning:

```bash
# Version 1.0
curl -H "Accept: application/vnd.api+json;version=1.0" http://localhost:8080/api/products

# Version 2.0
curl -H "Accept: application/vnd.api+json;version=2.0" http://localhost:8080/api/products
```

## Deprecation Strategy

Version 1.0 is marked as deprecated and includes the following RFC-compliant headers:

- **Deprecation**: `true` - Indicates the API version is deprecated
- **Sunset**: Date 6 months from now - When the API version will be retired
- **Link**: Points to the successor version with relationship metadata

Example response headers for v1.0:
```
Deprecation: true
Sunset: 2025-03-17
Link: </api/products>; rel="successor-version"; version="2.0"
```

## Configuration

The `ApiVersioningConfig` class demonstrates how to configure Spring Framework 7's API versioning:

```java
@Configuration
public class ApiVersioningConfig implements WebMvcConfigurer {
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.strategies(List.of(new HeaderContentNegotiationStrategy()));
    }
}
```

## Version Comparison

### Version 1.0 Response Format
```json
{
  "id": 1,
  "name": "MacBook Pro",
  "description": "Apple MacBook Pro 14-inch with M2 chip",
  "price": 1999.99,
  "inStock": true
}
```

### Version 2.0 Response Format
```json
{
  "id": 1,
  "name": "MacBook Pro",
  "description": "Apple MacBook Pro 14-inch with M2 chip",
  "price": 1999.99,
  "category": "Electronics",
  "inStock": true,
  "createdAt": "2024-08-18T10:30:00",
  "manufacturer": "Apple",
  "sku": "MBP-M2-14",
  "stockQuantity": 15
}
```

## Sample Data

The demo includes 5 sample products across different categories:
- MacBook Pro (Electronics)
- Gaming Chair (Furniture)
- Coffee Maker (Kitchen)
- Wireless Headphones (Electronics)
- Running Shoes (Sports)

## Best Practices Demonstrated

1. **Semantic Versioning**: Using clear version numbers (1.0, 2.0)
2. **Backward Compatibility**: V1 continues to work while V2 adds new features
3. **Clear Deprecation Timeline**: 6-month sunset period with proper headers
4. **Multiple Access Patterns**: Support for both header and path-based versioning
5. **RFC Compliance**: Following web standards for deprecation signaling
6. **Clean Architecture**: Separate response models for different versions

## Testing the Demo

1. Start the application: `./mvnw spring-boot:run`
2. Try the different versioning strategies with the curl commands above
3. Check the response headers for version 1.0 to see deprecation information
4. Compare the response formats between versions

## Extension Ideas

- Add query parameter versioning support
- Implement version negotiation with fallback strategies
- Add API documentation generation that respects versioning
- Create custom version validators for business rules
- Add metrics collection for version usage tracking