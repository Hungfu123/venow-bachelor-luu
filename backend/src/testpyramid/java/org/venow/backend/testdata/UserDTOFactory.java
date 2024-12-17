package org.venow.backend.testdata;

import org.venow.backend.service.dto.UserDTO;

import java.util.UUID;

public class UserDTOFactory {

    public static UserDTOWith createDefault() {
        UserDTO defaultUser = UserDTO.builder()
                .userId(UUID.randomUUID())
                .name("Luugner")
                .email("luugner@venow.com")
                .password("password")
                .role("ORGANIZER")
                .build();

        return new UserDTOWith(defaultUser);
    }

    public static class UserDTOWith {
        private UserDTO userDTO;

        public UserDTOWith(UserDTO userDTO) {
            this.userDTO = userDTO;
        }

        public UserDTOWith withUserId(UUID userId) {
            this.userDTO = userDTO.toBuilder().userId(userId).build();
            return this;
        }

        public UserDTOWith withEmail(String email) {
            this.userDTO = userDTO.toBuilder().email(email).build();
            return this;
        }

        public UserDTOWith withRole(String role) {
            this.userDTO = userDTO.toBuilder().role(role).build();
            return this;
        }

        public UserDTO build() {
            return this.userDTO;
        }
    }
}
