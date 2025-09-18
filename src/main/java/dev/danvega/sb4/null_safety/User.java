package dev.danvega.sb4.null_safety;

import org.jspecify.annotations.Nullable;

public record User(
        Long id,
        String name,
        @Nullable String email,
        @Nullable String phone,
        boolean active
) {

    public User {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
    }

    public static User createUser(String name) {
        return new User(null, name, null, null, true);
    }

    public static User createUserWithId(Long id, String name) {
        return new User(id, name, null, null, true);
    }

    public User withEmail(@Nullable String email) {
        return new User(this.id, this.name, email, this.phone, this.active);
    }

    public User withPhone(@Nullable String phone) {
        return new User(this.id, this.name, this.email, phone, this.active);
    }

    public boolean hasEmail() {
        return email != null && !email.isBlank();
    }

    public boolean hasPhone() {
        return phone != null && !phone.isBlank();
    }
}