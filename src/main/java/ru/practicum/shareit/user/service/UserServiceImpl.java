package ru.practicum.shareit.user.service;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exceptions.ConflictOperationException;
import ru.practicum.shareit.common.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;

    public List<User> findAll() {
        return userRepository.findAll().stream()
                .toList();
    }

    public User findById(@NotNull @Positive Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден. id=" + userId));
    }

    public User create(User user) {
        if (userRepository.isExistsByEmail(user)) {
            throw new ConflictOperationException(
                    "USER_EMAIL_CONFLICT",
                    "Пользователь с таким email (" + user.getEmail() + ") уже существует."
            );
        }
        User createdUser = userRepository.create(user);
        return findById(createdUser.getId());
    }

    public User update(Long userId, UpdateUserRequest dto) {
        User updateUser = findAndCheckEmail(userId, dto.getEmail());
        mapper.merge(dto, updateUser);
        return findById(updateUser.getId());
    }

    public void deleteById(@NotNull @Positive Long userId) {
        if (!userRepository.deleteById(userId)) {
            throw new NotFoundException("Пользователь не найден. id=" + userId);
        }
    }

    /* Helpers */
    private User findAndCheckEmail(Long userId, String email) {
        User foundUser = findById(userId);
        if (foundUser.getEmail() != null && !foundUser.getEmail().equals(email)) {
            if (userRepository.isExistsByEmail(email)) {
                throw new ConflictOperationException(
                        "UPDATE_USER_EMAIL_CONFLICT",
                        "Не могу обновить email: " + email + " уже используется."
                );
            }
        }
        return foundUser;
    }

}