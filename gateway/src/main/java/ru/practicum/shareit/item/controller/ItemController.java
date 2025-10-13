package ru.practicum.shareit.item.controller;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> findByOwnerId(
            @RequestHeader("X-Sharer-User-Id") @Positive long ownerId) {
        log.info("Find items user with id={}", ownerId);
        return itemClient.findByOwnerId(ownerId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(
            @RequestHeader(value = "X-Sharer-User-Id", required = false) @Positive Long viewerId,
            @PathVariable @Positive long itemId) {
        log.info("Find item with id={} user with id={}", itemId, viewerId);
        return itemClient.findById(viewerId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text) {
        log.info("Search query text={}", text);
        if (text == null || text.isBlank()) return ResponseEntity.ok(List.of());
        return itemClient.search(text);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(
            @RequestHeader("X-Sharer-User-Id") @Positive long ownerId,
            @RequestBody @Valid ItemCreateDto dto) {
        log.info("User id={} adding item {}", ownerId, dto);
        return itemClient.addItem(ownerId, dto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @RequestHeader("X-Sharer-User-Id") @Positive long authorId,
            @PathVariable @Positive long itemId,
            @RequestBody @Valid CommentCreateDto dto) {
        log.info("Adding comment {} from user with id={} to item with id={}", dto, authorId, itemId);
        return itemClient.addComment(authorId, itemId, dto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> patch(
            @RequestHeader("X-Sharer-User-Id") @Positive long ownerId,
            @PathVariable @Positive long itemId,
            @RequestBody @Valid ItemPatchDto dto) {
        log.info("Patching item with id={} with data={}, owner={}", itemId, dto, ownerId);
        return itemClient.patch(ownerId, itemId, dto);
    }
}
