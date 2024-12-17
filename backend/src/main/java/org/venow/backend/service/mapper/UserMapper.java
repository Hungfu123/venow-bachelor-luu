package org.venow.backend.service.mapper;

import org.venow.backend.domain.model.Role;
import org.venow.backend.domain.model.User;
import org.venow.backend.service.dto.UserDTO;

public class UserMapper {
    public static UserDTO mapToDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(String.valueOf(user.getRole()))
                .build();
    }

    public static User mapToEntity(UserDTO userDTO) {
        return User.builder()
                .userId(userDTO.userId())
                .name(userDTO.name())
                .email(userDTO.email())
                .password(userDTO.password())
                .role(Role.valueOf(userDTO.role()))
                .build();
    }
}
