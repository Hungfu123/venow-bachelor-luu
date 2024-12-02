package org.venow.backend.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.venow.backend.applicationservice.ApplicationDTO;
import org.venow.backend.applicationservice.ApplicationRepository;
import org.venow.backend.applicationservice.mapper.ApplicationMapper;
import org.venow.backend.domain.model.Application;

import java.util.Optional;
import java.util.UUID;

import static org.venow.backend.applicationservice.mapper.ApplicationMapper.mapToDTO;
import static org.venow.backend.applicationservice.mapper.ApplicationMapper.mapToEntity;

@Service
@Repository
@AllArgsConstructor
public class ApplicationRepositoryImpl implements ApplicationRepository {

    private final JpaApplicationRepository repository;

    @Override
    public ApplicationDTO save(ApplicationDTO applicationDto) {
        final Application savedApplication = repository.save(mapToEntity(applicationDto));

        return mapToDTO(savedApplication);
    }

    @Override
    public boolean existsByUserIdAndEventId(UUID userId, UUID eventId) {
        return repository.existsByUserIdAndEventId(userId,eventId);
    }

    @Override
    public Optional<ApplicationDTO> findById(UUID id) {
        return repository.findByApplicationId(id).map(ApplicationMapper::mapToDTO);
    }

}
