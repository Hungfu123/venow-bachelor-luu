package org.venow.backend.applicationservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.venow.backend.applicationservice.exception.ApplicationNotFoundException;
import org.venow.backend.applicationservice.exception.DuplicateApplicationException;
import org.venow.backend.applicationservice.exception.InvalidInputException;
import org.venow.backend.applicationservice.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class Applicationservice {

    private final ApplicationRepository applicationRepository;


    public ApplicationDTO createApplication(ApplicationDTO applicationDTO) {
        if (applicationRepository.existsByUserIdAndEventId(applicationDTO.userId(), applicationDTO.eventId())) {
            throw new DuplicateApplicationException("User has already applied for this event.");
        }

        final ApplicationDTO savedApplication = applicationRepository.save(applicationDTO);
        log.info("application created: {}", savedApplication);
        return savedApplication;
    }

    public List<ApplicationDTO> getAllApplicationsByOrganizer(UUID organizerId) {

        return applicationRepository.getAllApplicationsByOrganizer(organizerId);
    }

    public ApplicationDTO getApplication(UUID applicationId) {
        if (applicationId == null) {
            throw new InvalidInputException("Application ID must not be null");
        }

        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application not found for ID: " + applicationId));
    }


}
