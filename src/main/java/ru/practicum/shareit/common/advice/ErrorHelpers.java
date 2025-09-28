package ru.practicum.shareit.common.advice;

import ru.practicum.shareit.common.exceptions.BadRequestException;
import ru.practicum.shareit.common.exceptions.ConflictOperationException;
import ru.practicum.shareit.common.exceptions.ForbiddenOperationException;
import ru.practicum.shareit.common.exceptions.NotFoundException;

public class ErrorHelpers {
    public static RuntimeException notFound(String what, Object id) {
        return new NotFoundException(what + " не найден. id=" + id);
    }

    public static RuntimeException forbidden(String message) {
        return new ForbiddenOperationException(
                "FORBIDDEN",
                message);
    }

    public static RuntimeException conflict(String email) {
        return new ConflictOperationException(
                "USER_EMAIL_CONFLICT",
                "Пользователь с таким email (" + email + ") уже существует."
        );
    }

    public static RuntimeException badRequest(String message) {
        return new BadRequestException(
                "BAD_REQUEST",
                message
        );
    }
}
