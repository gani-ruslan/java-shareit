package ru.practicum.shareit.item.repository.jpa;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

@Repository
@Profile("jpa")
@RequiredArgsConstructor
public class ItemRepositoryJpaAdapter implements ItemRepository {

    private final ItemJpaRepository jpa;

    @Override
    public Item save(Item item) {
        return jpa.save(item);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public List<Item> findByOwner_Id(Long ownerId) {
        return jpa.findByOwnerId(ownerId);
    }

    @Override
    public List<Item> findByOwner_IdOrderByIdAsc(Long ownerId) {
        return jpa.findByOwnerIdOrderByIdAsc(ownerId);
    }

    @Override
    public List<Item> search(String text) {
        return jpa.search(text);
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public void delete(Item item) {
        jpa.delete(item);
    }

}
