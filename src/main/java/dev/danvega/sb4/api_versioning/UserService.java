package dev.danvega.sb4.api_versioning;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final List<User> users;

    public UserService() {
        this.users = initializeUsers();
    }

    public List<User> getAllUsers() {
        return users;
    }

    public Optional<User> getUserById(Integer id) {
        return users.stream()
                .filter(user -> user.id().equals(id))
                .findFirst();
    }

    private List<User> initializeUsers() {
        return List.of(
                new User(1, "Dan", "Vega", "dan@example.com"),
                new User(2, "Jane", "Smith", "jane@example.com"),
                new User(3, "John", "Doe", "john@example.com"),
                new User(4, "Sarah", "Johnson", "sarah@example.com"),
                new User(5, "Michael", "Brown", "michael@example.com")
        );
    }
}
