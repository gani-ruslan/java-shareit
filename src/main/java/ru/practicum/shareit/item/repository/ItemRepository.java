package ru.practicum.shareit.item.repository;

import java.util.List;
import ru.practicum.shareit.common.repository.BaseRepository;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository extends BaseRepository<Item,Long> {

    List<Item> findByOwnerId(Long ownerId);

    List<Item> findByQuery(String text);

}
