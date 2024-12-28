package org.venow.backend.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.venow.backend.domain.model.Contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaContractRepository extends JpaRepository<Contract, UUID> {

    Optional<Contract> findByContractId(final UUID id);

    @Query("SELECT c FROM contracts c WHERE c.organizer.userId = :organizerId")
    List<Contract> findAllByOrganizerId(UUID organizerId);


}
