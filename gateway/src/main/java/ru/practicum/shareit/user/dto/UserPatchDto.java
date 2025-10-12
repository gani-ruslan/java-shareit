package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserPatchDto(
        @Size(min = 4, max = 50, message = "Длина имени может быть от 4 до 50 символов")
        String name,

        @Email(message = "Некорректный формат адреса электронной почты")
        String email
) {}
