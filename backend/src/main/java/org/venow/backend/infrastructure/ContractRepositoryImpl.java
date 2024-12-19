package org.venow.backend.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.venow.backend.domain.model.Contract;
import org.venow.backend.service.ContractRepository;
import org.venow.backend.service.dto.ContractDTO;

import static org.venow.backend.service.mapper.ContractMapper.toDTO;
import static org.venow.backend.service.mapper.ContractMapper.toEntity;

@Repository
@AllArgsConstructor
public class ContractRepositoryImpl implements ContractRepository {

    private final JpaContractRepository repository;

    @Override
    public ContractDTO save(final ContractDTO contractDTO) {
        Contract save = repository.save(toEntity(contractDTO));
        return toDTO(save);
    }
}
