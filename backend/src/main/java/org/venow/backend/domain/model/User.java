package org.venow.backend.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity(name = "users")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(36)", name = "user_id")
    private final UUID userId;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private final String email;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private final String name;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private final String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private final Role role;
}
