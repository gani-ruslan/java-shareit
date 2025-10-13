package ru.practicum.shareit.user.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.ShortUserDto;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper {

    /**
     * In.
     */
    public User toEntity(UserCreateDto dto) {
        User u = new User();
        u.setName(dto.name());
        u.setEmail(dto.email());
        return u;
    }

    public void patchEntity(User entity, UserPatchDto dto) {
        if (dto.name() != null)
            entity.setName(dto.name());
        if (dto.email() != null)
            entity.setEmail(dto.email());
    }

    /**
     * Out.
     */
    public List<UserResponseDto> toResponse(List<User> users) {
        return users.stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponseDto toResponse(User u) {
        return new UserResponseDto(
            u.getId(),
            u.getName(),
            u.getEmail()
        );
    }

    public ShortUserDto toShort(User u) {
        return u == null
                ? null
                : new ShortUserDto(u.getId(), u.getName());
    }
}
