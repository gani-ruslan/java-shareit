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

    /* Base repository implementation*/
    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findById(Long userId) {
        return users.get(userId) == null
                ? Optional.empty()
                : Optional.of(users.get(userId));
    }

    @Override
    public User create(User user) {
        user.setId(counter.incrementAndGet());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), new User(user));
        return user;
    }

    @Override
    public boolean deleteById(Long userId) {
        return users.remove(userId) != null;
    }

    /* Extend base repository implementation */
    @Override
    public boolean isExistsByEmail(User user) {
        return getByEmail(user.getEmail()).isPresent();
    }

    @Override
    public boolean isExistsByEmail(String email) {
        return email != null && getByEmail(email).isPresent();
    }

    /* Helpers */
    private Optional<User> getByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();

    }
}
