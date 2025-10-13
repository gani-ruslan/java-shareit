package ru.practicum.shareit.booking.repository.memory;

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
@Profile("memory")
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingRepository {

    @Override
    public Booking save(Booking b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Booking> findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Booking> findAllForBooker(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Booking> findCurrentForBooker(Long userId, LocalDateTime ts) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Booking> findPastForBooker(Long userId, LocalDateTime ts) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Booking> findFutureForBooker(Long userId, LocalDateTime ts) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Booking> findByBookerAndStatus(Long userId, BookingStatus status) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Booking> findAllForOwner(Long ownerId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Booking> findCurrentForOwner(Long ownerId, LocalDateTime ts) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Booking> findPastForOwner(Long ownerId, LocalDateTime ts) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Booking> findFutureForOwner(Long ownerId, LocalDateTime ts) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Booking> findByOwnerAndStatus(Long ownerId, BookingStatus status) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean existsApprovedOverlap(Long itemId, LocalDateTime start, LocalDateTime end) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Booking> getLastApprovedBooking(Long bookerId, Long itemId, BookingStatus status) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Booking> findLastApprovedForItems(List<Long> itemIds, LocalDateTime ts) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Booking> findNextApprovedForItems(List<Long> itemIds, LocalDateTime ts) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
