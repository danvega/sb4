package dev.danvega.sb4.null_safety;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public UserRepository() {
        User john = new User(1L, "John Doe", "john@example.com", "+1234567890", true);
        User jane = new User(2L, "Jane Smith", null, "+0987654321", true);
        User bob = new User(3L, "Bob Johnson", "bob@example.com", null, false);

        users.put(john.id(), john);
        users.put(jane.id(), jane);
        users.put(bob.id(), bob);

        idGenerator.set(4);
    }

    public @Nullable User findById(Long id) {
        if (id == null) {
            return null;
        }
        return users.get(id);
    }

    public @Nullable User findByEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        return users.values().stream()
                .filter(user -> email.equals(user.email()))
                .findFirst()
                .orElse(null);
    }

    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    public List<User> findActiveUsers() {
        return users.values().stream()
                .filter(User::active)
                .toList();
    }

    public List<User> findUsersWithEmail() {
        return users.values().stream()
                .filter(User::hasEmail)
                .toList();
    }

    public List<User> findUsersWithPhone() {
        return users.values().stream()
                .filter(User::hasPhone)
                .toList();
    }

    public User save(User user) {
        User userToSave;
        if (user.id() == null) {
            Long newId = idGenerator.getAndIncrement();
            userToSave = new User(newId, user.name(), user.email(), user.phone(), user.active());
        } else {
            userToSave = user;
        }

        users.put(userToSave.id(), userToSave);
        return userToSave;
    }

    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }
        return users.remove(id) != null;
    }
}