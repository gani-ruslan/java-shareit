package ru.practicum.shareit.item.service;

import java.util.List;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;

public interface ItemService {
    List<Item> findByOwnerId(Long ownerId);

    Item findById(Long itemId);

    List<Item> findByQuery(String text);

    Item create(Long ownerId, Item item);

    Item update(Long ownerId, Long itemId, UpdateItemRequest dto);
}
