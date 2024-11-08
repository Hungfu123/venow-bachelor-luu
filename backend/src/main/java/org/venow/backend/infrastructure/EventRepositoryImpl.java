package org.venow.backend.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.venow.backend.applicationservice.EventRepository;
import org.venow.backend.domain.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class EventRepositoryImpl implements EventRepository {

    private final JpaEventRepository repository;


    @Override
    public List<Event> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Event> findById(UUID id) {
        return repository.findById(id);
    }
}
