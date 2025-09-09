package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.common.repository.BaseRepository;
import ru.practicum.shareit.user.model.User;

public interface UserRepository extends BaseRepository<User, Long> {

    boolean isExistsByEmail(User user);

    boolean isExistsByEmail(String email);

}
