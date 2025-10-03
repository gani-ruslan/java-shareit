package ru.practicum.shareit.booking.repository.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;

@Repository
@Profile("jpa")
@RequiredArgsConstructor
public class BookingRepositoryJpaAdapter implements BookingRepository {

    private final BookingJpaRepository jpa;

    @Override
    public Booking save(Booking b) {
        return jpa.save(b);
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public List<Booking> findAllForBooker(Long id) {
        return jpa.findByBooker_IdOrderByStartDateDesc(id);
    }

    @Override
    public List<Booking> findCurrentForBooker(Long userId, LocalDateTime ts) {
        return jpa.findByBooker_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(userId, ts, ts);
    }

    @Override
    public List<Booking> findPastForBooker(Long userId, LocalDateTime ts) {
        return jpa.findByBooker_IdAndEndDateBeforeOrderByStartDateDesc(userId, ts);
    }

    @Override
    public List<Booking> findFutureForBooker(Long userId, LocalDateTime ts) {
        return jpa.findByBooker_IdAndStartDateAfterOrderByStartDateDesc(userId, ts);
    }

    @Override
    public List<Booking> findByBookerAndStatus(Long userId, BookingStatus status) {
        return jpa.findByBooker_IdAndStatusOrderByStartDateDesc(userId, status);
    }

    @Override
    public List<Booking> findAllForOwner(Long ownerId) {
        return jpa.findByItem_Owner_IdOrderByStartDateDesc(ownerId);
    }

    @Override
    public List<Booking> findCurrentForOwner(Long ownerId, LocalDateTime ts) {
        return jpa.findByItem_Owner_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(ownerId, ts, ts);
    }

    @Override
    public List<Booking> findPastForOwner(Long ownerId, LocalDateTime ts) {
        return jpa.findByItem_Owner_IdAndEndDateBeforeOrderByStartDateDesc(ownerId, ts);
    }

    @Override
    public List<Booking> findFutureForOwner(Long ownerId, LocalDateTime ts) {
        return jpa.findByItem_Owner_IdAndStartDateAfterOrderByStartDateDesc(ownerId, ts);
    }

    @Override
    public List<Booking> findByOwnerAndStatus(Long ownerId, BookingStatus status) {
        return jpa.findByItem_Owner_IdAndStatusOrderByStartDateDesc(ownerId, status);
    }

    @Override
    public boolean existsApprovedOverlap(Long itemId, LocalDateTime start, LocalDateTime end) {
        return jpa.existsApprovedOverlap(itemId, start, end);
    }

    @Override
    public Optional<Booking> getLastApprovedBooking(Long bookerId, Long itemId, BookingStatus status) {
        return jpa.findTopByBooker_IdAndItem_IdAndStatusOrderByEndDateDesc(bookerId, itemId, status);
    }

    @Override
    public List<Booking> findLastApprovedForItems(List<Long> itemIds, LocalDateTime ts) {
        return jpa.findLastApprovedForItems(itemIds, ts);
    }

    @Override
    public List<Booking> findNextApprovedForItems(List<Long> itemIds, LocalDateTime ts) {
        return jpa.findNextApprovedForItems(itemIds, ts);
    }
}
