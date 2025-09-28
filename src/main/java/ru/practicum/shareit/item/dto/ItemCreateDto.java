package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ItemCreateDto(
    @NotBlank(message = "Название обязательно")
    @Size(min = 4, max = 50, message = "Длина названия может быть от 4 до 150 символов")
    String name,

    @NotBlank(message = "Описание обязательно")
    @Size(max = 200, message = "Длинна описания может быть не более 200 символов")
    String description,

    @NotNull(message = "Available флаг обязателен")
    Boolean available,

    Long requestId
) {}
