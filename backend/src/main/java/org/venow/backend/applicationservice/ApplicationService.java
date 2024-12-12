package org.venow.backend.applicationservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.venow.backend.applicationservice.exception.AlreadyRejectedException;
import org.venow.backend.applicationservice.exception.DuplicateApplicationException;
import org.venow.backend.applicationservice.exception.InvalidInputException;
import org.venow.backend.applicationservice.exception.NotFoundException;
import org.venow.backend.domain.model.Status;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationDTO createApplication(ApplicationDTO applicationDTO) {
        if (applicationDTO.getUserId() == null || applicationDTO.getApplicationId() == null || applicationDTO.getEventId() == null) {
            throw new InvalidInputException("User ID, Application ID, and Event ID must not be null");
        }
        if (applicationRepository.existsByUserIdAndEventId(applicationDTO.getUserId(), applicationDTO.getEventId())) {
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


    public ApplicationDTO rejectApplication(UUID applicationId) {
        final ApplicationDTO applicationDTO = getApplication(applicationId);
        if(applicationDTO.getStatus().equals(Status.REJECTED.name())){
            throw new AlreadyRejectedException("Application already rejected");
        }
        applicationDTO.setStatus(Status.REJECTED.name());
        return applicationRepository.save(applicationDTO);
    }
}
