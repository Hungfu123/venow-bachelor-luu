package org.venow.backend.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.venow.backend.domain.model.Application;
import org.venow.backend.service.ApplicationRepository;
import org.venow.backend.service.dto.ApplicationDTO;
import org.venow.backend.service.mapper.ApplicationMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.venow.backend.service.mapper.ApplicationMapper.mapToDTO;
import static org.venow.backend.service.mapper.ApplicationMapper.mapToEntity;

@Service
@Repository
@AllArgsConstructor
public class ApplicationRepositoryImpl implements ApplicationRepository {

    private final JpaApplicationRepository repository;

    @Override
    public ApplicationDTO save(final ApplicationDTO applicationDto) {
        final Application savedApplication = repository.save(mapToEntity(applicationDto));

        return mapToDTO(savedApplication);
    }

    @Override
    public boolean existsByUserIdAndEventId(final UUID userId, final UUID eventId) {
        return repository.existsByUserIdAndEventId(userId, eventId);
    }

    @Override
    public Optional<ApplicationDTO> findById(final UUID id) {
        return repository.findByApplicationId(id).map(ApplicationMapper::mapToDTO);
    }

    @Override
    public List<ApplicationDTO> getAllApplicationsByOrganizer(UUID organizerId) {
        return repository.findAllByOrganizerIdFromEvent(organizerId).stream().map(ApplicationMapper::mapToDTO).toList();
    }

    @Override
    public String getEventName(UUID eventId) {
        return repository.findEventNameByEventId(eventId);
    }

    @Override
    public List<ApplicationDTO> getAllApplicationsByExhibitorId(UUID exhibitorId) {
        return repository.findAllByUserId(exhibitorId).stream().map(ApplicationMapper::mapToDTO).toList();
    }

    @Override
    public List<ApplicationDTO> getAllApplicationsByOrganizerAndEventId(UUID organizerId, UUID eventId) {
        if (organizerId == null) {
            throw new IllegalArgumentException("Organizer ID must not be null");
        }
        if (eventId == null) {
            throw new IllegalArgumentException("Event ID must not be null");
        }

        return repository.findAllByOrganizerIdAndEventId(organizerId, eventId)
                .stream()
                .map(ApplicationMapper::mapToDTO)
                .toList();
    }


}
