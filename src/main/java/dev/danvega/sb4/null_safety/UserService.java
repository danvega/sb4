package dev.danvega.sb4.null_safety;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUserById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(userRepository.findById(id));
    }

    public Optional<User> findUserByEmail(@Nullable String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getActiveUsers() {
        return userRepository.findActiveUsers();
    }

    public List<User> getUsersWithContactInfo() {
        return userRepository.findAll().stream()
                .filter(user -> user.hasEmail() || user.hasPhone())
                .toList();
    }

    public User createUser(String name, @Nullable String email, @Nullable String phone) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }

        User newUser = User.createUser(name);
        if (email != null && !email.isBlank()) {
            newUser = newUser.withEmail(email);
        }
        if (phone != null && !phone.isBlank()) {
            newUser = newUser.withPhone(phone);
        }

        return userRepository.save(newUser);
    }

    public Optional<User> updateUserEmail(Long userId, @Nullable String newEmail) {
        if (userId == null) {
            return Optional.empty();
        }

        User existingUser = userRepository.findById(userId);
        if (existingUser == null) {
            return Optional.empty();
        }

        User updatedUser = existingUser.withEmail(newEmail);
        return Optional.of(userRepository.save(updatedUser));
    }

    public Optional<User> updateUserPhone(Long userId, @Nullable String newPhone) {
        if (userId == null) {
            return Optional.empty();
        }

        User existingUser = userRepository.findById(userId);
        if (existingUser == null) {
            return Optional.empty();
        }

        User updatedUser = existingUser.withPhone(newPhone);
        return Optional.of(userRepository.save(updatedUser));
    }

    public boolean deleteUser(Long userId) {
        if (userId == null) {
            return false;
        }
        return userRepository.deleteById(userId);
    }

    public String formatUserContact(User user) {
        StringBuilder contact = new StringBuilder(user.name());

        if (user.hasEmail()) {
            contact.append(" <").append(user.email()).append(">");
        }

        if (user.hasPhone()) {
            contact.append(" (").append(user.phone()).append(")");
        }

        return contact.toString();
    }

    public @Nullable String getUserPreferredContact(User user) {
        if (user.hasEmail()) {
            return user.email();
        }
        if (user.hasPhone()) {
            return user.phone();
        }
        return null;
    }

    public List<String> getUserContactMethods(User user) {
        return List.of(user.email(), user.phone()).stream()
                .filter(contact -> contact != null && !contact.isBlank())
                .toList();
    }
}