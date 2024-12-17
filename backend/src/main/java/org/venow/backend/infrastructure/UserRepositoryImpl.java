package org.venow.backend.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.venow.backend.domain.model.User;
import org.venow.backend.service.UserRepository;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.service.mapper.UserMapper;

import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository repository;


    @Override
    public Optional<UserDTO> findById(final UUID id) {
        return repository.findByUserId(id).map(UserMapper::mapToDTO);
    }

    @Override
    public UserDTO save(UserDTO user) {
        User savedUser = repository.save(UserMapper.mapToEntity(user));
        return UserMapper.mapToDTO(savedUser);
    }
}
