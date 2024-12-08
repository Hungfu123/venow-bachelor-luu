package org.venow.backend.builder;



import org.venow.backend.domain.model.Role;
import org.venow.backend.domain.model.User;

import java.util.UUID;

public class UserBuilder {
    private Long id = 1L;
    private UUID userId = UUID.randomUUID();
    private String email = "default@example.com";
    private String password = "password";
    private Role role = Role.EXHIBITOR;

    public UserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder withUserId(String userId) {
        this.userId = UUID.fromString(userId);
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withRole(Role role) {
        this.role = role;
        return this;
    }

    public User build() {
        return new User(id, userId, email, password, role);
    }
}

