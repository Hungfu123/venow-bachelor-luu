package org.venow.backend.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity(name = "events")
@Builder(toBuilder = true)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(36)")
    private final UUID eventId;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private final String title;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private final String description;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private final String location;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private final LocalDateTime startDate;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private final LocalDateTime endDate;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private final UUID organizerId;

}
