package ru.practicum.shareit.item.mapper;

import java.util.List;
import io.micrometer.common.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.comments.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {

    /**
     * In.
     */
    public Item toEntity(ItemCreateDto dto) {
        Item i = new Item();
        i.setName(dto.name());
        i.setDescription(dto.description());
        i.setAvailable(dto.available());
        return i;
    }

    public void patchEntity(Item entity, ItemPatchDto dto) {
        if (dto.name() != null)
            entity.setName(dto.name());
        if (dto.description() != null)
            entity.setDescription(dto.description());
        if (dto.available() != null)
            entity.setAvailable(dto.available());
    }

    /**
     * Out.
     */
    public List<ItemResponseDto> toSimpleResponse(List<Item> items) {
        return items.stream()
                .map(this::toSimpleResponse)
                .toList();
    }

    public ItemResponseDto toSimpleResponse(Item i) {
        return new ItemResponseDto(
            i.getId(),
            i.getName(),
            i.getDescription(),
            i.getAvailable(),
            i.getOwnerId(),
            i.getRequestId(),
  null, null, null
        );
    }

    public ItemResponseDto toFullResponse(Item item,
                                          @Nullable ShortBookingDto last,
                                          @Nullable ShortBookingDto next,
                                          List<CommentResponseDto> comments) {
        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwnerId(),
                item.getRequestId(),
                last,
                next,
                comments
        );
    }
}
