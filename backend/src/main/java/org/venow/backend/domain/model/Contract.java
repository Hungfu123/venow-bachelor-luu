package org.venow.backend.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.venow.backend.infrastructure.converter.PriceConverter;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity(name = "contracts")
@Builder(toBuilder = true)
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private final UUID contractId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id", referencedColumnName = "user_id", nullable = false, columnDefinition = "VARCHAR(36)")
    private final User organizer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibitor_id", referencedColumnName = "user_id", nullable = false, columnDefinition = "VARCHAR(36)")
    private final User exhibitor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", referencedColumnName = "application_id", nullable = false)
    private final Application application;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private final String terms;

    @Convert(converter = PriceConverter.class)
    @Column
    private final Price price;

    @Column
    private final String boothNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private final Status status;



}
