package dev.danvega.sb4.api_versioning.dto;

import dev.danvega.sb4.api_versioning.User;

public record UserDTOv1(
        Integer id,
        String name,
        String email
) {
    public static UserDTOv1 fromUser(User user) {
        return new UserDTOv1(
                user.id(),
                user.firstName() + " " + user.lastName(),
                user.email()
        );
    }
}
