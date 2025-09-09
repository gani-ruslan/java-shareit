package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

@Data
public class UpdateItemRequest implements ItemPayload {
    @Size(min = 4, max = 50, message = "Длина названия может быть от 4 до 150 символов")
    String name;

    @Size(max = 200, message = "Длинна описания может быть не более 200 символов")
    String description;

    Boolean available;

    User owner;

    Request request;
}
