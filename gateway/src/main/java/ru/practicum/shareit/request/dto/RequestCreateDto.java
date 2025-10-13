package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestCreateDto(
        @NotBlank(message = "Описание обязательно")
        @Size(min = 4, max = 1000, message = "Длина описания может быть от 4 до 1000 символов")
        String description
) {}
