package ru.practicum.shareit.common.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends AppException {
    public BadRequestException(String code, String message) {
        super(HttpStatus.BAD_REQUEST, code, message);
    }
}
