package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest implements UserPayload {
    @NotBlank(message = "Имя обязательно")
    @Size(min = 4, max = 50, message = "Длина имени может быть от 4 до 50 символов")
    String name;

    @NotBlank(message = "E-mail обязательно")
    @Email(message = "Некорректный формат адреса электронной почты")
    String email;
}
