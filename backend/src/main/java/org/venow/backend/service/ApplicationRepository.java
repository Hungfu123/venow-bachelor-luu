package org.venow.backend.service;

import org.venow.backend.service.dto.ApplicationDTO;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository {

    ApplicationDTO save(ApplicationDTO application);

    boolean existsByUserIdAndEventId(UUID uuid, UUID uuid1);

    Optional<ApplicationDTO> findById(UUID id);

    List<ApplicationDTO> getAllApplicationsByOrganizer(UUID organizerId);

    List<ApplicationDTO> getAllApplicationsByOrganizerAndEventId(UUID organizerId, UUID eventId);
    String getEventName(UUID eventId);

    List<ApplicationDTO> getAllApplicationsByExhibitorId(UUID exhibitorId);
}
