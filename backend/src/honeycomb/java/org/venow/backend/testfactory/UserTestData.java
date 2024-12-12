package org.venow.backend.testfactory;


import org.venow.backend.domain.model.User;

import java.util.UUID;

public class UserTestData {
    public static User createExhibitor1() {
        return User.builder()
                .id(1L)
                .userId(UUID.fromString("df333e19-6ed6-4c8b-b86e-ef78943a98a4"))
                .email("showman@example.com")
                .role(org.venow.backend.domain.model.Role.EXHIBITOR)
                .build();
    }
    public static User createExhibitor2() {
        return User.builder()
                .id(5L)
                .userId(UUID.fromString("45353065-3834-3030-2d65-3239633d3431"))
                .email("showmantest3@example.com")
                .role(org.venow.backend.domain.model.Role.EXHIBITOR)
                .build();
    }

    public static User createExhibitor3() {
        return User.builder()
                .id(6L)
                .userId(UUID.fromString("bf333e19-6ed6-4c8b-b86e-ef78943a98a4"))
                .email("showmantest4@example.com")
                .role(org.venow.backend.domain.model.Role.EXHIBITOR)
                .build();
    }

    public static User createOrganizer1() {
        return User.builder()
                .id(2L)
                .userId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .email("organizer1@example.com")
                .role(org.venow.backend.domain.model.Role.ORGANIZER)
                .build();
    }

    public static User createOrganizer2() {
        return User.builder()
                .id(3L)
                .userId(UUID.fromString("b23e4567-e89b-12d3-a456-426614174001"))
                .email("organizer2@example.com")
                .role(org.venow.backend.domain.model.Role.ORGANIZER)
                .build();
    }
    public static User createOrganizer3() {
        return User.builder()
                .id(4L)
                .userId(UUID.fromString("b23e4567-e89b-12d3-a456-426614174002"))
                .email("organizer3@example.com")
                .role(org.venow.backend.domain.model.Role.ORGANIZER)
                .build();
    }
}
