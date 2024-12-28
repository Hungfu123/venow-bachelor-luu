package org.venow.backend.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.venow.backend.domain.model.Role;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.service.exception.NotFoundException;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDTO getUserByUserId(final UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found for ID: " + id));
    }

    public boolean existsByUserAndRole(UUID organizerId, Role role) {
        return userRepository.existsByUserIdAndRole(organizerId, role);
    }


}
