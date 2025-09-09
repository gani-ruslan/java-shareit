package ru.practicum.shareit.item.web;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CreateItemRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemServiceImpl;

/**
 * Item controller
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemServiceImpl itemService;
    private final ItemMapper mapper;

    @GetMapping
    public List<ItemDto> findByOwnerId(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return mapper.toDto(itemService.findByOwnerId(ownerId));
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable Long itemId) {
        return mapper.toDto(itemService.findById(itemId));
    }

    @GetMapping("/search")
    public List<ItemDto> findByQuery(@RequestParam String text) {
        return mapper.toDto(itemService.findByQuery(text));
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                          @RequestBody @Validated CreateItemRequest item) {
        return mapper.toDto(itemService.create(ownerId, mapper.toDomainCreate(item)));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                             @PathVariable Long itemId,
                             @RequestBody @Validated UpdateItemRequest item) {
        return mapper.toDto(itemService.update(ownerId, itemId, item));
    }
}
