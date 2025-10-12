package ru.practicum.shareit.item.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository {
    Item save(Item item);

    Optional<Item> findById(Long id);

    List<Item> findByOwner_Id(Long ownerId);

    List<Item> findByOwner_IdOrderByIdAsc(Long ownerId);

    List<Item> search(String text);

    void deleteById(Long id);

    void delete(Item item);

    List<Item> findAllByRequestId(Long requestId);

    List<Item> findAllByRequestIdIn(Collection<Long> requestsId);
}