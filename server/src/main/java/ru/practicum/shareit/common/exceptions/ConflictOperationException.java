package ru.practicum.shareit.common.exceptions;

import org.springframework.http.HttpStatus;

public class ConflictOperationException extends AppException {
    public ConflictOperationException(String code, String message) {
        super(HttpStatus.CONFLICT, code, message);
    }
}
