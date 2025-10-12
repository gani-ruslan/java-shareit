package ru.practicum.shareit.common.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenOperationException extends AppException {
    public ForbiddenOperationException(String code, String message) {
        super(HttpStatus.FORBIDDEN, code, message);
    }
}
