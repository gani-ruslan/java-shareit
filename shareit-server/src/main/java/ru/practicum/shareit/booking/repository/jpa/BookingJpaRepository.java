package ru.practicum.shareit.booking.repository.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

public interface BookingJpaRepository extends JpaRepository<Booking, Long> {

    @EntityGraph(value = "Booking.withItemBooker")
    @NonNull
    Optional<Booking> findById(@NonNull Long id);

    // Booker lists
    @EntityGraph(value = "Booking.withItemBooker")
    List<Booking> findByBooker_IdOrderByStartDateDesc(Long userId);

    // CURRENT
    @EntityGraph(value = "Booking.withItemBooker")
    List<Booking> findByBooker_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
            Long userId, LocalDateTime now1, LocalDateTime now2);

    // PAST
    @EntityGraph(value = "Booking.withItemBooker")
    List<Booking> findByBooker_IdAndEndDateBeforeOrderByStartDateDesc(
            Long userId, LocalDateTime now);

    // FUTURE
    @EntityGraph(value = "Booking.withItemBooker")
    List<Booking> findByBooker_IdAndStartDateAfterOrderByStartDateDesc(
            Long userId, LocalDateTime now);

    // WAITING/REJECTED
    @EntityGraph(value = "Booking.withItemBooker")
    List<Booking> findByBooker_IdAndStatusOrderByStartDateDesc(
            Long userId, BookingStatus status);

    // Owner lists
    @EntityGraph(value = "Booking.withItemBooker")
    List<Booking> findByItem_Owner_IdOrderByStartDateDesc(Long ownerId);

    // CURRENT
    @EntityGraph(value = "Booking.withItemBooker")
    List<Booking> findByItem_Owner_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
            Long ownerId, LocalDateTime now1, LocalDateTime now2);

    // PAST
    @EntityGraph(value = "Booking.withItemBooker")
    List<Booking> findByItem_Owner_IdAndEndDateBeforeOrderByStartDateDesc(
            Long ownerId, LocalDateTime now);

    // FUTURE
    @EntityGraph(value = "Booking.withItemBooker")
    List<Booking> findByItem_Owner_IdAndStartDateAfterOrderByStartDateDesc(
            Long ownerId, LocalDateTime now);

    // WAITING/REJECTED
    @EntityGraph(value = "Booking.withItemBooker")
    List<Booking> findByItem_Owner_IdAndStatusOrderByStartDateDesc(
            Long ownerId, BookingStatus status);

    // Cross-checks
    @Query("""
        select count(b) > 0
        from Booking b
        where b.item.id = :itemId
          and b.status = ru.practicum.shareit.booking.model.BookingStatus.APPROVED
          and b.startDate < :end
          and b.endDate   > :start
        """)
    boolean existsApprovedOverlap(Long itemId, LocalDateTime start, LocalDateTime end);

    // BookingRepository.java
    Optional<Booking> findTopByBooker_IdAndItem_IdAndStatusOrderByEndDateDesc(
            Long bookerId, Long itemId, BookingStatus status);

    @Query("""
    select b from Booking b
    where b.item.id in :itemIds
      and b.status = ru.practicum.shareit.booking.model.BookingStatus.APPROVED
      and b.endDate < :ts
    order by b.item.id asc, b.endDate desc
    """)
    List<Booking> findLastApprovedForItems(List<Long> itemIds, LocalDateTime ts);

    @Query("""
    select b from Booking b
    where b.item.id in :itemIds
      and b.status = ru.practicum.shareit.booking.model.BookingStatus.APPROVED
      and b.startDate > :now
    order by b.item.id asc, b.startDate asc
    """)
    List<Booking> findNextApprovedForItems(List<Long> itemIds, LocalDateTime now);
}
