package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ItemCreateDto(
        @NotBlank(message = "Название обязательно")
        @Size(min = 1, max = 255, message = "Длина названия может быть от 1 до 255 символов")
        String name,

        @NotBlank(message = "Описание обязательно")
        @Size(max = 1000, message = "Длина описания может быть не более 1000 символов")
        String description,

        @NotNull(message = "Available флаг обязателен")
        Boolean available,

        Long requestId
) {}