package ru.practicum.shareit.comments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateDto(
        @NotBlank(message = "текст сообщения обязателен")
        @Size(max = 1000, message = "текст сообщения не может быть более 1000 символов")
        String text
){}
