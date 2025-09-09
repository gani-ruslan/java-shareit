package ru.practicum.shareit.item.repository.memory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ItemRepositoryImplTest {

    private final ItemRepositoryImpl repo = new ItemRepositoryImpl();

    @Test
    @DisplayName("create: назначает id и сохраняет в хранилище")
    void create_assignsIdAndPersists() {
        Item item = newItem(10L, "Hammer", "Steel", true);

        Item saved = repo.create(item);

        assertNotNull(saved.getId());
        Optional<Item> found = repo.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Hammer", found.get().getName());
    }

    @Test
    @DisplayName("findById: пусто для несуществующего id")
    void findById_emptyWhenNotExists() {
        assertTrue(repo.findById(999L).isEmpty());
    }

    @Test
    @DisplayName("update: кладёт копию Item (последующие изменения исходника не протекают)")
    void update_storesDefensiveCopy() {
        Item item = repo.create(newItem(10L, "A", "x", true));
        Long id = item.getId();

        Item toUpdate = new Item(item); // скопировали, поменяли имя
        toUpdate.setName("B");
        repo.update(toUpdate);

        // Меняем исходный объект после update — в репозитории не должно поменяться
        toUpdate.setName("C");

        Item fromRepo = repo.findById(id).orElseThrow();
        assertEquals("B", fromRepo.getName());
    }

    @Test
    @DisplayName("findByOwnerId: возвращает только вещи конкретного владельца")
    void findByOwnerId_filtersByOwner() {
        repo.create(newItem(10L, "A", "x", true));
        repo.create(newItem(10L, "B", "y", true));
        repo.create(newItem(11L, "C", "z", true));

        List<Item> items = repo.findByOwnerId(10L);

        assertEquals(2, items.size());
        assertTrue(items.stream().anyMatch(i -> i.getName().equals("A")));
        assertTrue(items.stream().anyMatch(i -> i.getName().equals("B")));
        assertTrue(items.stream().noneMatch(i -> i.getName().equals("C")));
    }

    @Test
    @DisplayName("findAll: сейчас не поддерживается → UnsupportedOperationException")
    void findAll_unsupported() {
        assertThrows(UnsupportedOperationException.class, repo::findAll);
    }

    @Test
    @DisplayName("deleteById: сейчас не поддерживается → UnsupportedOperationException")
    void deleteById_unsupported() {
        assertThrows(UnsupportedOperationException.class, () -> repo.deleteById(1L));
    }

    @Test
    @DisplayName("findByQuery: ищет и по NAME, и по DESCRIPTION; регистр игнорируется; возвращает только available=true")
    void findByQuery_nameAndDescription_caseInsensitive_onlyAvailable() {
        repo.create(newItem(10L, "ГИТара", "красная", true));               // match по name
        repo.create(newItem(10L, "Чехол", "гитара в комплекте", true));     // match по description
        repo.create(newItem(10L, "Гитара", "новая", false));                // не должен вернуться: unavailable
        repo.create(newItem(10L, "Стол", "деревянный", true));              // нет совпадения

        List<Item> res = repo.findByQuery("гИтАрА");

        assertEquals(2, res.size());
        assertTrue(res.stream().anyMatch(i -> i.getName().equals("ГИТара")));
        assertTrue(res.stream().anyMatch(i -> i.getName().equals("Чехол")));
        assertTrue(res.stream().noneMatch(i -> i.getName().equals("Гитара"))); // отфильтрован по available=false
        assertTrue(res.stream().noneMatch(i -> i.getName().equals("Стол")));
    }

    @Test
    @DisplayName("findByQuery: blank / пробельный / null запрос → пустой список")
    void findByQuery_blankOrNull_returnsEmpty() {
        assertTrue(repo.findByQuery("").isEmpty());
        assertTrue(repo.findByQuery("   ").isEmpty());
        assertTrue(repo.findByQuery(null).isEmpty());
    }

    @Test
    @DisplayName("findByQuery: null в name/description обрабатывается безопасно; матч по другому полю учитывается")
    void findByQuery_handlesNullFieldsSafely() {
        // null description, но совпадение по name
        repo.create(newItem(10L, "Drill", null, true));
        // null name, но совпадение по description
        repo.create(newItem(10L, null, "Cordless drill", true));
        // оба поля null → никогда не совпадёт
        repo.create(newItem(10L, null, null, true));

        List<Item> res = repo.findByQuery("drill");

        assertEquals(2, res.size());
        assertTrue(res.stream().anyMatch(i -> "Drill".equals(i.getName())));
        assertTrue(res.stream().anyMatch(i -> "Cordless drill".equals(i.getDescription())));
    }

    @Test
    @DisplayName("findByQuery: когда совпадений нет — возвращает пустой список")
    void findByQuery_noMatch_returnsEmpty() {
        repo.create(newItem(10L, "Hammer", "Steel", true));
        assertTrue(repo.findByQuery("violin").isEmpty());
    }

    /* Helpers */
    private static Item newItem(long ownerId, String name, String desc, boolean available) {
        User owner = new User();
        owner.setId(ownerId);
        owner.setName("u" + ownerId);
        owner.setEmail("u" + ownerId + "@ex.org");

        Item i = new Item();
        i.setName(name);
        i.setDescription(desc);
        i.setAvailable(available);
        i.setOwner(owner);
        return i;
    }
}
