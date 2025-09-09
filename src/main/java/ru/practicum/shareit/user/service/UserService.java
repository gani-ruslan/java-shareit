package ru.practicum.shareit.user.service;

import java.util.List;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.model.User;

public interface UserService {
    List<User> findAll();

    User findById(Long id);

    User create(User user);

    User update(Long id, UpdateUserRequest dto);

    void deleteById(Long id);
}
