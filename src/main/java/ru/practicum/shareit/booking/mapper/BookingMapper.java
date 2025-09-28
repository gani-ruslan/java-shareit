package ru.practicum.shareit.booking.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.user.dto.ShortUserDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Component
public class BookingMapper {
    /**
     * In.
     */
    public Booking toEntity(BookingCreateDto dto) {
        Booking b = new Booking();
        b.setItemId(dto.itemId());
        b.setStartDate(dto.start());
        b.setEndDate(dto.end());
        return b;
    }

    /**
     * Out.
     */
    public List<BookingResponseDto> toResponse(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toResponse)
                .toList();
    }

    public BookingResponseDto toResponse(Booking b) {
        return new BookingResponseDto(
                b.getId(),
                b.getStartDate(),
                b.getEndDate(),
                b.getStatus(),
                toShort(b.getBooker()),
                toShort(b.getItem())
        );
    }

    /* Helpers */
    private ShortUserDto toShort(User u) {
        return u == null
                ? null
                : new ShortUserDto(u.getId(), u.getName());
    }

    private ShortItemDto toShort(Item i) {
        return i == null
                ? null
                : new ShortItemDto(i.getId(), i.getName());
    }
}