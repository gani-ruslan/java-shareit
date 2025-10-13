package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.user.dto.ShortUserDto;

public record BookingResponseDto(
   Long id,
   LocalDateTime start,
   LocalDateTime end,
   BookingStatus status,
   ShortUserDto booker,
   ShortItemDto item
) {}
