package ru.practicum.shareit.user.repository.memory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryImplTest {

    private final UserRepositoryImpl repo = new UserRepositoryImpl();

    @Test
    @DisplayName("create: назначает id, сохраняет, виден в findById и findAll")
    void create_assignsId_andVisible() {
        User u = newUser("Alice", "a@ex.org");

        User saved = repo.create(u);

        assertNotNull(saved.getId());
        Optional<User> found = repo.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getName());

        List<User> all = repo.findAll();
        assertEquals(1, all.size());
        assertEquals(saved.getId(), all.get(0).getId());
    }

    @Test
    @DisplayName("findById: пусто для несуществующего id")
    void findById_emptyWhenNotExists() {
        assertTrue(repo.findById(999L).isEmpty());
    }

    @Test
    @DisplayName("update: кладёт копию User (последующие изменения исходника не протекают)")
    void update_storesDefensiveCopy() {
        User u = repo.create(newUser("Bob", "b@ex.org"));
        Long id = u.getId();

        User toUpdate = new User(u);
        toUpdate.setName("Bobby");
        repo.update(toUpdate);

        // Меняем исходник после update — в репозитории не должно поменяться
        toUpdate.setName("ChangedAfterUpdate");

        User fromRepo = repo.findById(id).orElseThrow();
        assertEquals("Bobby", fromRepo.getName());
    }

    @Test
    @DisplayName("deleteById: true для существующего, false для отсутствующего")
    void deleteById_trueWhenExists_falseOtherwise() {
        User saved = repo.create(newUser("C", "c@ex.org"));
        assertTrue(repo.deleteById(saved.getId()));
        assertFalse(repo.deleteById(saved.getId())); // повторное удаление
        assertTrue(repo.findById(saved.getId()).isEmpty());
    }

    /* Helpers */
    private static User newUser(String name, String email) {
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        return u;
    }
}
