package org.venow.backend.testfactory;

import org.venow.backend.builder.UserBuilder;
import org.venow.backend.domain.model.User;

public class UserTestData {
    public static User createDefaultUser() {
        return new UserBuilder()
                .withId(1L)
                .withUserId("df333e19-6ed6-4c8b-b86e-ef78943a98a4")
                .withEmail("showman@example.com")
                .withRole(org.venow.backend.domain.model.Role.EXHIBITOR)
                .build();
    }

    public static User createOrganizer1() {
        return new UserBuilder()
                .withId(2L)
                .withUserId("123e4567-e89b-12d3-a456-426614174000")
                .withEmail("organizer1@example.com")
                .withRole(org.venow.backend.domain.model.Role.ORGANIZER)
                .build();
    }

    public static User createOrganizer2() {
        return new UserBuilder()
                .withId(3L)
                .withUserId("b23e4567-e89b-12d3-a456-426614174001")
                .withEmail("organizer2@example.com")
                .withRole(org.venow.backend.domain.model.Role.ORGANIZER)
                .build();
    }
    public static User createOrganizer3() {
        return new UserBuilder()
                .withId(4L)
                .withUserId("b23e4567-e89b-12d3-a456-426614174002")
                .withEmail("organizer3@example.com")
                .withRole(org.venow.backend.domain.model.Role.ORGANIZER)
                .build();
    }
}
