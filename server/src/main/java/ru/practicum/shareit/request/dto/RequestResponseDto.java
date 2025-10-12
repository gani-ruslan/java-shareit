package ru.practicum.shareit.request.dto;

import java.time.LocalDateTime;

public record RequestResponseDto(
    Long id,
    String description,
    Long requesterId,
    LocalDateTime created
) {}
