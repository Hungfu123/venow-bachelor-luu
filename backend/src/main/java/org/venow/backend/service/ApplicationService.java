package org.venow.backend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.venow.backend.service.dto.ApplicationDTO;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.service.exception.AlreadyRejectedException;
import org.venow.backend.service.exception.DuplicateApplicationException;
import org.venow.backend.service.exception.InvalidInputException;
import org.venow.backend.service.exception.NotFoundException;
import org.venow.backend.domain.model.Status;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserService userService;

    public ApplicationDTO createApplication(final ApplicationDTO applicationDTO) {
        if (applicationDTO.getUserId() == null ||  applicationDTO.getEventId() == null) {
            throw new InvalidInputException("User ID and Event ID must not be null");
        }
        if (applicationRepository.existsByUserIdAndEventId(applicationDTO.getUserId(), applicationDTO.getEventId())) {
            throw new DuplicateApplicationException("User has already applied for this event.");
        }

        final ApplicationDTO savedApplication = applicationRepository.save(applicationDTO);
        log.info("application created: {}", savedApplication);
        savedApplication.setEventTitle(applicationRepository.getEventName(savedApplication.getEventId()));
        return savedApplication;
    }

    public List<ApplicationDTO> getAllApplicationsByOrganizer(final UUID organizerId) {

        return applicationRepository.getAllApplicationsByOrganizer(organizerId).stream()
                .map(
                        applicationDTO -> {
                            applicationDTO.setEventTitle(applicationRepository.getEventName(applicationDTO.getEventId()));
                            return applicationDTO;
                        }
                ).toList();
    }

    public ApplicationDTO getApplication(final UUID applicationId) {
        if (applicationId == null) {
            throw new InvalidInputException("Application ID must not be null");
        }

        final ApplicationDTO applicationDTO = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application not found for ID: " + applicationId));

        final String eventName = applicationRepository.getEventName(applicationDTO.getEventId());
        applicationDTO.setEventTitle(eventName);
        return applicationDTO;
    }


    public ApplicationDTO rejectApplication(final UUID applicationId) {
        final ApplicationDTO applicationDTO = getApplication(applicationId);
        if(applicationDTO.getStatus().equals(Status.REJECTED.name())){
            throw new AlreadyRejectedException("Application already rejected");
        }
        applicationDTO.setStatus(Status.REJECTED.name());
        ApplicationDTO savedApplication = applicationRepository.save(applicationDTO);
        savedApplication.setEventTitle(applicationRepository.getEventName(applicationDTO.getEventId()));
        return savedApplication;
    }

    public ApplicationDTO acceptApplication(final UUID applicationId) {
        final ApplicationDTO applicationDTO = getApplication(applicationId);
        if(applicationDTO.getStatus().equals(Status.ACCEPTED.name()) || applicationDTO.getStatus().equals(Status.REJECTED.name())){
            throw new AlreadyRejectedException("Application already accepted or rejected");
        }
        applicationDTO.setStatus(Status.ACCEPTED.name());
        applicationDTO.setEventTitle(applicationRepository.getEventName(applicationDTO.getEventId()));
        return applicationRepository.save(applicationDTO);
    }

    public String getApplicationName(UUID applicationId) {
        ApplicationDTO applicationDTO = getApplication(applicationId);
        UserDTO user = userService.getUserByUserId(applicationDTO.getUserId());
        return user.name();
    }
}
