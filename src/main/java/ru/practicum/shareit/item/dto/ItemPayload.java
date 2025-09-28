package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

public interface ItemPayload {
    String getName();

    String getDescription();

    Boolean getAvailable();

    User getOwner();

    Request getRequest();
}
