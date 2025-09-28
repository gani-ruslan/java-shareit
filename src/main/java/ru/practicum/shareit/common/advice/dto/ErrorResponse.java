package ru.practicum.shareit.common.advice.dto;

import java.time.Instant;

public record ErrorResponse(String code, String message, String path, Instant timestamp) {
}
