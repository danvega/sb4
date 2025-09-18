# JSpecify Null Safety Demo

This package demonstrates the JSpecify null safety annotations available in Spring Framework 7 and Spring Boot 4.

## Key Features Demonstrated

### Package-level Null Safety
- `package-info.java` with `@NullMarked` annotation enables null safety checking for the entire package
- By default, all types are considered non-null unless explicitly marked with `@Nullable`

### Model with Mixed Nullability
- `User` record shows mix of required (id, name) and optional (email, phone) fields
- Constructor validation ensures non-null constraints are enforced
- Helper methods like `hasEmail()` and `hasPhone()` provide safe null checks

### Repository Layer
- Methods return `@Nullable` for single entity lookups that might not find results
- Collection methods return non-null lists (empty if no results)
- Defensive null checking in all method parameters

### Service Layer
- Uses `Optional<T>` for return types that might be empty
- Explicit null parameter handling with early returns
- Methods that accept `@Nullable` parameters handle them safely

### Controller Layer
- REST endpoints with proper null validation
- Request/Response DTOs with nullable fields clearly marked
- Appropriate HTTP status codes for null/invalid requests

## API Endpoints

- `GET /api/null-safety/users` - Get all users
- `GET /api/null-safety/users/active` - Get active users only
- `GET /api/null-safety/users/with-contact` - Get users with email or phone
- `GET /api/null-safety/users/{id}` - Get user by ID
- `GET /api/null-safety/users/by-email?email=` - Find user by email
- `POST /api/null-safety/users` - Create new user
- `PUT /api/null-safety/users/{id}/email` - Update user email
- `PUT /api/null-safety/users/{id}/phone` - Update user phone
- `DELETE /api/null-safety/users/{id}` - Delete user
- `GET /api/null-safety/users/{id}/contact-info` - Get formatted contact info

## Benefits of JSpecify

1. **Compile-time Safety**: Null safety violations can be caught at build time with tools like NullAway
2. **Clear API Contracts**: Method signatures clearly indicate which parameters and return values can be null
3. **IDE Support**: Better IDE assistance with null-safety warnings and autocompletion
4. **Documentation**: Code self-documents its null-safety contracts
5. **Interoperability**: Works with Kotlin and other JVM languages that understand nullability annotations

## Example Usage

```java
// Safe - all non-null parameters
User user = userService.createUser("John Doe", "john@example.com", "+1234567890");

// Safe - nullable email explicitly handled
Optional<User> found = userService.findUserByEmail(possiblyNullEmail);
if (found.isPresent()) {
    // Use found user
}

// Safe - nullable return value handled
String preferredContact = userService.getUserPreferredContact(user);
if (preferredContact != null) {
    // Use contact info
}
```