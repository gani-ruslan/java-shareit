package ru.practicum.shareit.booking.mapper;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

@Component
@AllArgsConstructor
public class BookingMapper {
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

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
                userMapper.toShort(b.getBooker()),
                itemMapper.toShort(b.getItem())
        );
    }
}