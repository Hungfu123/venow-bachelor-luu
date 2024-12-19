package org.venow.backend.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.venow.backend.domain.model.Contract;
import org.venow.backend.service.dto.ContractDTO;

import java.util.UUID;

public interface JpaContractRepository extends JpaRepository<Contract, UUID> {

    ContractDTO save(final ContractDTO contractDTO);
}
