package dev.danvega.sb4.null_safety;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/null-safety/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/active")
    public List<User> getActiveUsers() {
        return userService.getActiveUsers();
    }

    @GetMapping("/with-contact")
    public List<User> getUsersWithContact() {
        return userService.getUsersWithContactInfo();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-email")
    public ResponseEntity<User> getUserByEmail(@RequestParam @Nullable String email) {
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> user = userService.findUserByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            User createdUser = userService.createUser(
                    request.name(),
                    request.email(),
                    request.phone()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/email")
    public ResponseEntity<User> updateUserEmail(
            @PathVariable Long id,
            @RequestBody UpdateEmailRequest request) {

        Optional<User> updatedUser = userService.updateUserEmail(id, request.email());
        return updatedUser.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/phone")
    public ResponseEntity<User> updateUserPhone(
            @PathVariable Long id,
            @RequestBody UpdatePhoneRequest request) {

        Optional<User> updatedUser = userService.updateUserPhone(id, request.phone());
        return updatedUser.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/contact-info")
    public ResponseEntity<ContactInfoResponse> getUserContactInfo(@PathVariable Long id) {
        Optional<User> userOpt = userService.findUserById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        String formattedContact = userService.formatUserContact(user);
        String preferredContact = userService.getUserPreferredContact(user);
        List<String> contactMethods = userService.getUserContactMethods(user);

        ContactInfoResponse response = new ContactInfoResponse(
                formattedContact,
                preferredContact,
                contactMethods
        );

        return ResponseEntity.ok(response);
    }

    public record CreateUserRequest(
            String name,
            @Nullable String email,
            @Nullable String phone
    ) {}

    public record UpdateEmailRequest(@Nullable String email) {}

    public record UpdatePhoneRequest(@Nullable String phone) {}

    public record ContactInfoResponse(
            String formattedContact,
            @Nullable String preferredContact,
            List<String> contactMethods
    ) {}
}