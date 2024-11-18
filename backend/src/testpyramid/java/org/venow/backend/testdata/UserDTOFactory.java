package org.venow.backend.testdata;

import org.venow.backend.applicationservice.UserDTO;

import java.util.UUID;

public class UserDTOFactory {

    public static UserDTO.UserDTOBuilder defaultBuilder() {
        return UserDTO.builder()
                .userId(UUID.randomUUID())
                .email("luugner@venow.com")
                .role("USER");
    }

    public static UserDTO createDefault() {
        return defaultBuilder().build();
    }

    public static UserDTO createWithCustomEmail(String email) {
        return defaultBuilder()
                .build();
    }

    public static UserDTO createWithExhibitorRole() {
        return defaultBuilder()
                .role("EXHIBITOR")
                .build();
    }
    public static UserDTO createWithOrganizerRole() {
        return defaultBuilder()
                .role("ORGANIZER")
                .build();
    }

}
