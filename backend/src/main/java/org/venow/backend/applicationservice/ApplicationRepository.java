package org.venow.backend.applicationservice;

import org.venow.backend.domain.model.Application;

import java.util.UUID;

public interface ApplicationRepository {

    ApplicationDTO save(ApplicationDTO application);

    boolean existsByUserIdAndEventId(UUID uuid, UUID uuid1);
}
