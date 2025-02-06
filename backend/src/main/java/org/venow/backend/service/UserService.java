package org.venow.backend.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.venow.backend.config.CustomUserDetails;
import org.venow.backend.domain.model.Role;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.service.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDTO getUserByUserId(final UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found for ID: " + id));
    }

    public boolean existsByUserAndRole(UUID organizerId, Role role) {
        return userRepository.existsByUserIdAndRole(organizerId, role);
    }

    public UserDTO registerUser(UserDTO user) {
        user = user.toBuilder()
                .userId(UUID.randomUUID())
                .password(passwordEncoder.encode(user.password()))
                .build();
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return new CustomUserDetails(
                user.userId(),
                user.email(),
                user.password(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.role()))
        );
    }


    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}


