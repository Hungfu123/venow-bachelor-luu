package org.venow.backend.applicationservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.venow.backend.applicationservice.exception.DuplicateApplicationException;

import static org.venow.backend.applicationservice.mapper.ApplicationMapper.mapToEntity;
@Slf4j
@Service
@AllArgsConstructor
public class Applicationservice {

    private final ApplicationRepository repository;


    public ApplicationDTO createApplication(ApplicationDTO applicationDTO) {
        if (repository.existsByUserIdAndEventId(applicationDTO.userId(), applicationDTO.eventId())) {
            throw new DuplicateApplicationException("User has already applied for this event.");
        }

        final ApplicationDTO savedApplication = repository.save(applicationDTO);
        log.info("application created: {}", savedApplication);
        return savedApplication;
    }
}
