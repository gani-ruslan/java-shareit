package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Size;

public record ItemPatchDto(
    @Size(min = 4, max = 50, message = "Длина названия может быть от 4 до 150 символов")
    String name,

    @Size(max = 200, message = "Длинна описания может быть не более 200 символов")
    String description,

    Boolean available,

    Long ownerId,

    Long requestId
) {}
