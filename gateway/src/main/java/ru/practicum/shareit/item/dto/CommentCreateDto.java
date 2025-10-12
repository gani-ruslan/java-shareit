package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateDto(
        @NotBlank(message = "Текст сообщения обязателен")
        @Size(max = 1000, message = "Текст сообщения не может быть более 1000 символов")
        String text
){}
