package org.venow.backend.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.venow.backend.domain.model.Contract;
import org.venow.backend.service.ContractRepository;
import org.venow.backend.service.dto.ContractDTO;
import org.venow.backend.service.mapper.ContractMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public Optional<ContractDTO> findById(final UUID id) {
        return repository.findByContractId(id).map(ContractMapper::toDTO);
    }

    @Override
    public List<ContractDTO> findAllByOrganizerId(final UUID organizerId) {
        return repository.findAllByOrganizerId(organizerId).stream()
                .map(ContractMapper::toDTO)
                .toList();
    }

    @Override
    public List<ContractDTO> findAllByExhibitorId(UUID exhibitorId) {
        return repository.findAllByExhibitorId(exhibitorId).stream()
                .map(ContractMapper::toDTO)
                .toList();
    }


}
