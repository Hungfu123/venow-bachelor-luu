package org.venow.backend.service.dto;

import lombok.Builder;

import java.util.UUID;
@Builder(toBuilder = true)
public record UserDTO(
        UUID userId,
        String name,
        String password,
        String email,
        String role
) {}
