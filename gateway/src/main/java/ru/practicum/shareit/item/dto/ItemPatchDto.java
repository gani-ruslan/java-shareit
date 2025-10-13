package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Size;

public record ItemPatchDto(
        @Size(min = 1, max = 255, message = "Длина названия может быть от 1 до 255 символов")
        String name,

        @Size(max = 1000, message = "Длина описания может быть не более 1000 символов")
        String description,

        Boolean available,

        Long ownerId
) {}
