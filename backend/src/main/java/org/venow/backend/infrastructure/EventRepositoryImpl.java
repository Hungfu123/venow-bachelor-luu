package org.venow.backend.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.venow.backend.applicationservice.EventDTO;
import org.venow.backend.applicationservice.EventRepository;
import org.venow.backend.applicationservice.mapper.EventMapper;
import org.venow.backend.domain.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class EventRepositoryImpl implements EventRepository {

    private final JpaEventRepository repository;

    @Override
    public List<EventDTO> findAll() {
        return repository.findAll().stream().map(EventMapper::mapToEventDTO).toList();
    }

    @Override
    public Optional<EventDTO> findById(final UUID id) {
        return repository.findEventByEventId(id).map(EventMapper::mapToEventDTO);
    }

    @Override
    public EventDTO save(final EventDTO event) {
        final Event savedEvent = repository.save(EventMapper.mapToEntity(event));

        return EventMapper.mapToEventDTO(savedEvent);


    }

}
