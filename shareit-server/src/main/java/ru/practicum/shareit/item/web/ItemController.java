package ru.practicum.shareit.item.web;

import java.util.List;
import jakarta.validation.Valid;
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
import ru.practicum.shareit.comments.dto.CommentCreateDto;
import ru.practicum.shareit.comments.dto.CommentResponseDto;
import ru.practicum.shareit.comments.service.CommentService;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.service.ItemService;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @GetMapping
    public List<ItemResponseDto> findByOwnerId(
            @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.findByOwnerId(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto findById(
            @RequestHeader(value = "X-Sharer-User-Id", required = false) Long viewerId,
            @PathVariable Long itemId) {
        return itemService.findById(viewerId, itemId);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @PostMapping
    public ItemResponseDto addItem(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestBody @Valid ItemCreateDto dto) {
        return itemService.addItem(ownerId, dto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(
            @RequestHeader("X-Sharer-User-Id") Long authorId,
            @PathVariable Long itemId,
            @RequestBody @Valid CommentCreateDto dto) {
        return commentService.addComment(itemId, authorId, dto);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto patch(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @PathVariable Long itemId,
            @RequestBody @Valid ItemPatchDto dto) {
        return itemService.patch(ownerId, itemId, dto);
    }
}
