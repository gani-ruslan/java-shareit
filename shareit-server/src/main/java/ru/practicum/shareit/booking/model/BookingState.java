package ru.practicum.shareit.booking.model;

import static ru.practicum.shareit.common.advice.ErrorHelpers.badRequest;

public enum BookingState {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static BookingState from(String raw) {
        if (raw == null) return ALL;
        try {
            return BookingState.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw badRequest(raw);
        }
    }
}