package ru.practicum.shareit.item.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CreateItemRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPayload;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {

    /**
     * In.
     */
    public Item toDomainCreate(CreateItemRequest dto) {
        return createCommonItem(dto);
    }

    public void merge(UpdateItemRequest dto, Item target) {
        if (dto == null || target == null) return;
        if (dto.getName() != null)         target.setName(dto.getName());
        if (dto.getDescription() != null)  target.setDescription(dto.getDescription());
        if (dto.getAvailable() != null)    target.setAvailable(dto.getAvailable());
        if (dto.getOwner() != null)        target.setOwner(dto.getOwner());
        if (dto.getRequest() != null)      target.setRequest(dto.getRequest());
    }

    /**
     * Out.
     */
    public ItemDto toDto(Item item) {
        return createDto(item);
    }

    public List<ItemDto> toDto(List<Item> items) {
        return items.stream()
                .map(this::createDto)
                .toList();
    }

    /**
     * Helpers.
     */
    private ItemDto createDto(Item item) {
        return new ItemDto(
            item.getId(),
            item.getName(),
            item.getDescription(),
            item.getAvailable(),
            item.getOwner(),
            item.getRequest()
        );
    }

    private <T extends ItemPayload> Item createCommonItem(T dto) {
        return new Item(
            null,
            dto.getName(),
            dto.getDescription(),
            dto.getAvailable(),
            dto.getOwner(),
            dto.getRequest()
        );
    }
}
