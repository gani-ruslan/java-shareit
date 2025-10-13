package ru.practicum.shareit.request.dto;

import java.time.LocalDateTime;
import java.util.List;
import ru.practicum.shareit.item.dto.ShortItemDto;

public record RequestFullResponseDto(
    Long id,
    String description,
    Long requesterId,
    LocalDateTime created,
    List<ShortItemDto> items
) {}
