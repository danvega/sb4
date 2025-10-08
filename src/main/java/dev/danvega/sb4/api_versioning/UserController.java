package dev.danvega.sb4.api_versioning;

import dev.danvega.sb4.api_versioning.dto.UserDTOv1;
import dev.danvega.sb4.api_versioning.dto.UserDTOv2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{version}/users/", version = "1.0")
    public ResponseEntity<List<UserDTOv1>> getAllUsersV1() {
        List<UserDTOv1> users = userService.getAllUsers()
                .stream()
                .map(UserDTOv1::fromUser)
                .toList();

        HttpHeaders headers = createDeprecationHeaders();
        return ResponseEntity.ok().headers(headers).body(users);
    }

    @GetMapping(value = "/users", version = "2.0")
    public List<UserDTOv2> getAllUsersV2() {
        return userService.getAllUsers()
                .stream()
                .map(UserDTOv2::fromUser)
                .toList();
    }

    @GetMapping(value = "/users/{id}", version = "1.0")
    public ResponseEntity<UserDTOv1> getUserByIdV1(@PathVariable Integer id) {
        return userService.getUserById(id)
                .map(user -> {
                    HttpHeaders headers = createDeprecationHeaders();
                    return ResponseEntity.ok().headers(headers).body(UserDTOv1.fromUser(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/users/{id}", version = "2.0")
    public ResponseEntity<UserDTOv2> getUserByIdV2(@PathVariable Integer id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(UserDTOv2.fromUser(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    private HttpHeaders createDeprecationHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Deprecation", "true");
        headers.add("Sunset", LocalDate.now().plusMonths(6).toString());
        headers.add("Link", "</api/users>; rel=\"successor-version\"; version=\"2.0\"");
        return headers;
    }
}
