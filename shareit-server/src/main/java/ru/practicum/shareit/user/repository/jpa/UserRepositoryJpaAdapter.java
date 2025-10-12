package ru.practicum.shareit.user.repository.jpa;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Repository
@Profile("jpa")
@RequiredArgsConstructor
public class UserRepositoryJpaAdapter implements UserRepository {

    private final UserJpaRepository jpa;

    @Override
    public User save(User user) {
        return jpa.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public List<User> findAll() {
        return jpa.findAll();
    }

    public void deleteById(Long id) {
        jpa.deleteById(id);
    }

    public Boolean existsByEmail(String email) {
        return jpa.existsByEmail(email);
    }
}
