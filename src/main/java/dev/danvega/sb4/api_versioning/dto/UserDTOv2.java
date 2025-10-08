package dev.danvega.sb4.api_versioning.dto;

import dev.danvega.sb4.api_versioning.User;

public record UserDTOv2(
        Integer id,
        String firstName,
        String lastName,
        String email
) {
    public static UserDTOv2 fromUser(User user) {
        return new UserDTOv2(
                user.id(),
                user.firstName(),
                user.lastName(),
                user.email()
        );
    }
}
