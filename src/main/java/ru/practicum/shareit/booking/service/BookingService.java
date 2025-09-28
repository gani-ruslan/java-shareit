package ru.practicum.shareit.booking.service;

import static ru.practicum.shareit.common.advice.ErrorHelpers.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper mapper;
    private final Clock clock;

    @Transactional
    public BookingResponseDto save(Long bookerId, BookingCreateDto dto) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> notFound("User", bookerId));

        Item item = itemRepository.findById(dto.itemId())
                .orElseThrow(() -> notFound("Item", dto.itemId()));

        if (!Boolean.TRUE.equals(item.getAvailable()))
            throw badRequest("Вещь недоступна для бронирования");

        if (item.getOwner().getId().equals(bookerId))
            throw forbidden("Владелец не может бронировать свою вещь");

        if (!dto.start().isBefore(dto.end()))
            throw badRequest("Начальная дата не может быть позже конечной даты бронирования");

        boolean overlap = bookingRepository.existsApprovedOverlap(item.getId(), dto.start(), dto.end());
        if (overlap)
            throw badRequest("Время бронирования пересекается с уже имеющимся интервалом");

        Booking b = mapper.toEntity(dto);
        b.setBooker(booker);
        b.setItem(item);
        b.setStatus(BookingStatus.WAITING);

        Booking saved = bookingRepository.save(b);
        return mapper.toResponse(saved);
    }

    @Transactional
    public BookingResponseDto approve(Long ownerId, Long bookingId, boolean approved) {
        Booking b = bookingRepository.findById(bookingId)
                .orElseThrow(() -> notFound("Booking", bookingId));

        if (!b.getItem().getOwner().getId().equals(ownerId))
            throw forbidden("Только владелец подтвердить бронирование");

        if (b.getStatus() != BookingStatus.WAITING)
            throw badRequest("Бронирование уже подтверждено");

        b.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return mapper.toResponse(b);
    }

    public BookingResponseDto findById(Long requesterId, Long bookingId) {
        Booking b = bookingRepository.findById(bookingId)
                .orElseThrow(() -> notFound("Booking", bookingId));

        Long ownerId = b.getItem().getOwner().getId();
        Long bookerId = b.getBooker().getId();

        if (!requesterId.equals(ownerId) && !requesterId.equals(bookerId)) {
            throw forbidden("Информацию может получить либо тот кто бронирует, либо владелец вещи.");
        }
        return mapper.toResponse(b);
    }

    public List<BookingResponseDto> listForBooker(Long bookerId, String stateRaw) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> notFound("User", bookerId));

        BookingState state = BookingState.from(stateRaw);
        LocalDateTime now = LocalDateTime.now(clock);

        List<Booking> result = switch (state) {
            case ALL      -> bookingRepository.findAllForBooker(booker.getId());
            case CURRENT  -> bookingRepository.findCurrentForBooker(booker.getId(), now);
            case PAST     -> bookingRepository.findPastForBooker(booker.getId(), now);
            case FUTURE   -> bookingRepository.findFutureForBooker(booker.getId(), now);
            case WAITING  -> bookingRepository.findByBookerAndStatus(booker.getId(), BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findByBookerAndStatus(booker.getId(), BookingStatus.REJECTED);
        };
        return mapper.toResponse(result);
    }

    public List<BookingResponseDto> listForOwner(Long ownerId, String stateRaw) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> notFound("User", ownerId));

        BookingState state = BookingState.from(stateRaw);
        LocalDateTime now = LocalDateTime.now(clock);

        List<Booking> result = switch (state) {
            case ALL      -> bookingRepository.findAllForOwner(owner.getId());
            case CURRENT  -> bookingRepository.findCurrentForOwner(owner.getId(), now);
            case PAST     -> bookingRepository.findPastForOwner(owner.getId(), now);
            case FUTURE   -> bookingRepository.findFutureForOwner(owner.getId(), now);
            case WAITING  -> bookingRepository.findByOwnerAndStatus(owner.getId(), BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findByOwnerAndStatus(owner.getId(), BookingStatus.REJECTED);
        };
        return mapper.toResponse(result);
    }
}
