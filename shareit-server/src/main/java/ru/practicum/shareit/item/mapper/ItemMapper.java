package ru.practicum.shareit.item.mapper;

import java.util.List;
import io.micrometer.common.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.comments.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

@Component
public class ItemMapper {

    /**
     * In.
     */
    public Item toEntity(ItemCreateDto dto, User owner, Request request) {
        Item i = new Item();
        i.setName(dto.name());
        i.setDescription(dto.description());
        i.setAvailable(dto.available());
        i.setOwner(owner);
        i.setRequest(request);
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
    public ItemResponseDto toSimpleResponse(Item i) {
        return new ItemResponseDto(
            i.getId(),
            i.getName(),
            i.getDescription(),
            i.getAvailable(),
            i.getOwnerId(),
            (i.getRequest() != null ? i.getRequest().getId() : i.getRequestId()),
  null, null, null
        );
    }

    public ItemResponseDto toFullResponse(Item i,
                                          @Nullable ShortBookingDto last,
                                          @Nullable ShortBookingDto next,
                                          List<CommentResponseDto> comments) {
        return new ItemResponseDto(
                i.getId(),
                i.getName(),
                i.getDescription(),
                i.getAvailable(),
                i.getOwnerId(),
                (i.getRequest() != null ? i.getRequest().getId() : i.getRequestId()),
                last,
                next,
                comments
        );
    }

    public ShortItemDto toShort(Item i) {
        return i == null
                ? null
                : new ShortItemDto(i.getId(), i.getName(), i.getOwnerId());
    }

}
