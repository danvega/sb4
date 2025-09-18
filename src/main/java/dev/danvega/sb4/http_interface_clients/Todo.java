package dev.danvega.sb4.http_interface_clients;

public record Todo(
        Long id,
        Long userId,
        String title,
        Boolean completed
) {
}