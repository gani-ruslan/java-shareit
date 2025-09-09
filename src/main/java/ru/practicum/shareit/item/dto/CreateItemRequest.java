package ru.practicum.shareit.item.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateItemRequest implements ItemPayload {
    @NotBlank(message = "Название обязательно")
    @Size(min = 4, max = 50, message = "Длина названия может быть от 4 до 150 символов")
    String name;

    @NotBlank(message = "Описание обязательно")
    @Size(max = 200, message = "Длинна описания может быть не более 200 символов")
    String description;

    @NotNull(message = "Available флаг обязателен")
    Boolean available;

    User owner;

    Request request;
}
