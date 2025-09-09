package ru.practicum.shareit.item.repository.memory;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

@Repository
@Profile("memory")
public class ItemRepositoryImpl implements ItemRepository {

    private final ConcurrentHashMap<Long, Item> items = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong();

    /* Base repository implementation*/
    @Override
    public List<Item> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Item> findById(Long itemId) {
        return items.get(itemId) == null
                ? Optional.empty()
                : Optional.of(items.get(itemId));
    }

    @Override
    public Item create(Item item) {
        item.setId(counter.incrementAndGet());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), new Item(item));
        return item;
    }

    @Override
    public boolean deleteById(Long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* Extend base repository implementation */
    @Override
    public List<Item> findByOwnerId(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .toList();
    }

    @Override
    public List<Item> findByQuery(String text) {
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> search(item.getDescription(), text) ||
                                     search(item.getName(), text))
                .toList();
    }

    /* Helpers */
    private boolean search(String source, String target) {
        if (target == null || target.isBlank() || source == null) {
            return false;
        }
        return source.toLowerCase(Locale.ROOT)
                .contains(target.toLowerCase(Locale.ROOT));
    }
}