package org.venow.backend.applicationservice;

import lombok.Builder;

import java.util.UUID;
@Builder
public record UserDTO(
        UUID userId,
        String email,
        String role
) {}
