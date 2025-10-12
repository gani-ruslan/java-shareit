package ru.practicum.shareit.user.repository.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Repository
@Profile("memory")
public class UserRepositoryImpl implements UserRepository {

    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong();

    @Override
    public User save(User user) {
        user.setId(counter.incrementAndGet());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long userId) {
        return users.get(userId) == null
                ? Optional.empty()
                : Optional.of(users.get(userId));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(Long userId) {
        users.remove(userId);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return email != null && getByEmail(email).isPresent();
    }

    /* Helpers */
    private Optional<User> getByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
}
