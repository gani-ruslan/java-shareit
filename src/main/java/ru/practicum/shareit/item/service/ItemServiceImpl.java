package ru.practicum.shareit.item.service;

import java.util.List;
import java.util.Objects;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exceptions.ForbiddenOperationException;
import ru.practicum.shareit.common.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper mapper;

    public List<Item> findByOwnerId(Long ownerId) {
        return itemRepository.findByOwnerId(ownerId).stream()
                .toList();
    }

    public Item findById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет не найден. id=" + itemId));
    }

    public List<Item> findByQuery(String query) {
        return itemRepository.findByQuery(query);
    }

    public Item create(Long ownerId, @Valid Item item) {
        User owner = userService.findById(ownerId);
        item.setOwner(owner);
        Item createdItem = itemRepository.create(item);
        return findById(createdItem.getId());
    }

    public Item update(Long ownerId, Long itemId, @Valid UpdateItemRequest dto) {
        Item updateItem = findAndCheckOwner(ownerId, itemId);
        mapper.merge(dto, updateItem);
        return findById(itemRepository.update(updateItem).getId());
    }

    /* Helpers */
    private Item findAndCheckOwner(Long ownerId, Long itemId) {
        User owner = userService.findById(ownerId);
        Item foundItem = findById(itemId);
        if (foundItem.getOwner() != null && !Objects.equals(foundItem.getOwner().getId(), owner.getId())) {
            throw new ForbiddenOperationException(
                    "ITEM_OWNER_MISMATCH",
                    "Недостаточно прав для изменения предмета " + itemId
            );
        }
        return foundItem;
    }

}