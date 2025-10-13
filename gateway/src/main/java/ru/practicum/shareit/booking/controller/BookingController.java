package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(
            @RequestHeader("X-Sharer-User-Id") @Positive long userId,
            @PathVariable @Positive Long bookingId) {
        log.info("Find booking data from user with id={} for booking data id={}", userId, bookingId);
        return bookingClient.findById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> listForBookers(
            @RequestHeader("X-Sharer-User-Id") @Positive long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String bookingState) {

        log.info("Find bookers from user with id={} with state={}", userId, bookingState);

        String state = bookingState == null ? "ALL" : bookingState.trim();
        if (state.isEmpty()) state = "ALL";

        BookingState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + bookingState));

        return bookingClient.listForBookers(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> listForOwner(
            @RequestHeader("X-Sharer-User-Id") @Positive long ownerId,
            @RequestParam(name = "state",  defaultValue = "ALL") String bookingState) {

        log.info("Find bookers from owner with id={} with state={}", ownerId, bookingState);
        String state = bookingState == null ? "ALL" : bookingState.trim();
        if (state.isEmpty()) state = "ALL";

        BookingState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + bookingState));

        return bookingClient.listForOwner(ownerId, state);
    }

    @PostMapping
    public ResponseEntity<Object> save(
            @RequestHeader("X-Sharer-User-Id") @Positive long bookerId,
            @RequestBody @Valid BookingCreateDto dto) {
        log.info("Create booking from user id={} booking {}", bookerId, dto);
        return bookingClient.save(bookerId, dto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(
            @RequestHeader("X-Sharer-User-Id") @Positive Long ownerId,
            @PathVariable @Positive long bookingId,
            @RequestParam("approved") boolean approved) {
        log.info("Patching from user with id={} with booking data with id={} to approve={}", ownerId, bookingId, approved);
        return bookingClient.approve(ownerId, bookingId, approved);
    }
}
