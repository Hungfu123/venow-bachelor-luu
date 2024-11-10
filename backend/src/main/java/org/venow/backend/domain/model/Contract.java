package org.venow.backend.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.venow.backend.infrastructure.converter.PriceConverter;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity(name = "contracts")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private final UUID contractId;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private final String terms;

    @Convert(converter = PriceConverter.class)
    @Column(nullable = false)
    private final Price price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private final Status status;



}
