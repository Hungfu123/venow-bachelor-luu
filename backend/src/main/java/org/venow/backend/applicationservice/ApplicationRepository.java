package org.venow.backend.applicationservice;

import org.venow.backend.applicationservice.dto.ApplicationDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository {

    ApplicationDTO save(ApplicationDTO application);

    boolean existsByUserIdAndEventId(UUID uuid, UUID uuid1);

    Optional<ApplicationDTO> findById(UUID id);

    List<ApplicationDTO> getAllApplicationsByOrganizer(UUID organizerId);

    ApplicationDTO updateStatus(ApplicationDTO applicationDTO);
}
