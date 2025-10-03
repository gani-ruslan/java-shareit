package ru.practicum.shareit.comments.dto;

import java.time.LocalDateTime;

public record CommentResponseDto(
        Long id,
        String text,
        Long itemId,
        LocalDateTime created,
        String authorName
) {}
