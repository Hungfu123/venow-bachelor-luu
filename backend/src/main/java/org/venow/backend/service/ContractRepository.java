package org.venow.backend.service;

import org.venow.backend.service.dto.ContractDTO;

import java.util.Optional;
import java.util.UUID;

public interface ContractRepository  {


    ContractDTO save(ContractDTO contractDTO);

    Optional<ContractDTO> findById(UUID id);
}
