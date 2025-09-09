package ru.practicum.shareit.common.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestOperationException extends AppException {
    public BadRequestOperationException(String code, String message) {
        super(HttpStatus.BAD_REQUEST, code, message);
    }
}
