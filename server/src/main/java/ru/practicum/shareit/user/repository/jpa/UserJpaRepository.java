package ru.practicum.shareit.user.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
