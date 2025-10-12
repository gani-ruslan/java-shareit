package ru.practicum.shareit.item.repository.memory;

import java.util.Collection;
import java.util.List;
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

    @Override
    public Item save(Item item) {
        item.setId(counter.incrementAndGet());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> findById(Long itemId) {
        return items.get(itemId) == null
                ? Optional.empty()
                : Optional.of(items.get(itemId));
    }

    @Override
    public List<Item> findByOwner_Id(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .toList();
    }

    @Override
    public List<Item> findByOwner_IdOrderByIdAsc(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .toList();
    }

    @Override
    public List<Item> search(String text) {
        String q = text.toLowerCase();
        return items.values().stream()
                .filter(i -> Boolean.TRUE.equals(i.getAvailable()))
                .filter(i -> i.getName().toLowerCase().contains(q) ||
                        (i.getDescription() != null && i.getDescription().toLowerCase().contains(q)))
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        items.remove(id);
    }

    @Override
    public void delete(Item item) {
        items.remove(item.getId());
    }

    @Override
    public List<Item> findAllByRequestId(Long requestId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Item> findAllByRequestIdIn(Collection<Long> requestsId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}