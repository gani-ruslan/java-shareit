package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public record BookingCreateDto(
    @NotNull(message = "id предмета обязателен")
    Long itemId,

    @NotNull(message = "время начала бронирования обязательно")
    @FutureOrPresent(message = "время начала должно быть в настоящем или будущем")
    LocalDateTime start,

    @NotNull(message = "время окончания бронирования обязательно")
    @Future(message = "время окончания должно быть в будущем")
    LocalDateTime end
) {}
