package org.venow.backend.applicationservice.dto;

import lombok.Builder;

import java.util.UUID;
@Builder(toBuilder = true)
public record UserDTO(
        UUID userId,
        String email,
        String role
) {}
