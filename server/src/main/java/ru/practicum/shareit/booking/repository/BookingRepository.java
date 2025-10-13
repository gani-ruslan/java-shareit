package ru.practicum.shareit.booking.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

public interface BookingRepository {
    Booking save(Booking booking);

    Optional<Booking> findById(Long id);

    void deleteById(Long id);

    // Booker lists
    List<Booking> findAllForBooker(Long userId);

    List<Booking> findCurrentForBooker(Long userId, LocalDateTime ts);

    List<Booking> findPastForBooker(Long userId, LocalDateTime ts);

    List<Booking> findFutureForBooker(Long userId, LocalDateTime ts);

    List<Booking> findByBookerAndStatus(Long userId, BookingStatus status);

    // Owner lists
    List<Booking> findAllForOwner(Long ownerId);

    List<Booking> findCurrentForOwner(Long ownerId, LocalDateTime ts);

    List<Booking> findPastForOwner(Long ownerId, LocalDateTime ts);

    List<Booking> findFutureForOwner(Long ownerId, LocalDateTime ts);

    List<Booking> findByOwnerAndStatus(Long ownerId, BookingStatus status);

    // Cross-checks
    boolean existsApprovedOverlap(Long itemId, LocalDateTime start, LocalDateTime end);

    Optional<Booking> getLastApprovedBooking(Long bookerId, Long itemId, BookingStatus status);

    // Last/Next booking data
    List<Booking> findLastApprovedForItems(List<Long> itemIds, LocalDateTime ts);

    List<Booking> findNextApprovedForItems(List<Long> itemIds, LocalDateTime ts);
}
