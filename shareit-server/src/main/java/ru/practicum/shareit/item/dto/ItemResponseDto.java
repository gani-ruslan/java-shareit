package ru.practicum.shareit.item.dto;

import java.util.List;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.comments.dto.CommentResponseDto;

public record ItemResponseDto(
    Long id,
    String name,
    String description,
    Boolean available,
    Long ownerId,
    Long requestId,
    ShortBookingDto lastBooking,
    ShortBookingDto nextBooking,
    List<CommentResponseDto> comments
) {}
