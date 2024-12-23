package org.venow.backend.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity(name = "applications")
@Table(
        name = "applications",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"userId", "eventId"}),
                @UniqueConstraint(columnNames = {"application_id"})

        }
)
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_id", nullable = false)
    private UUID applicationId;

    @Column(nullable = false)
    private final UUID userId;

    @Column(nullable = false, columnDefinition = "VARCHAR(36)")
    private final UUID eventId;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private final String contactInformation;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private final String businessName;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private final String typeOfBusiness;

    @Column(nullable = false, columnDefinition = "BOOLEAN")
    private final boolean previousParticipation;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private final String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private final Status status;
}

