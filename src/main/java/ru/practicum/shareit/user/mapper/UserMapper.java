package ru.practicum.shareit.user.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserPayload;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper {

    /**
     * In.
     */
    public User toDomainCreate(CreateUserRequest dto) {
        return createCommonUser(dto);
    }

    public void merge(UpdateUserRequest dto, User target) {
        if (dto == null || target == null) return;
        if (dto.getName() != null)   target.setName(dto.getName());
        if (dto.getEmail() != null)  target.setEmail(dto.getEmail());
    }

    /**
     * Out.
     */
    public UserDto toDto(User user) {
        return createDto(user);
    }

    public List<UserDto> toDto(List<User> users) {
        return users.stream()
                .map(this::createDto)
                .toList();
    }

    /**
     * Helpers.
     */
    private UserDto createDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    private <T extends UserPayload> User createCommonUser(T dto) {
        return new User(
                null,
                dto.getName(),
                dto.getEmail()
        );
    }
}
