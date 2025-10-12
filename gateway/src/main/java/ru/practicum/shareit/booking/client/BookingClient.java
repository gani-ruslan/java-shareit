package ru.practicum.shareit.booking.client;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.common.client.BaseClient;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> findById(Long userId, Long bookingId) {

        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> listForBookers(Long userId, String state) {
        return get("?state={state}", userId, Map.of("state", state));
    }

    public ResponseEntity<Object> listForOwner(Long userId, String state) {
        return get("/owner/?state={state}", userId, Map.of("state", state));
    }

    public ResponseEntity<Object> save(long bookerId, BookingCreateDto dto) {
        return post("", bookerId, dto);
    }

    public ResponseEntity<Object> approve(long ownerId, long bookingId, boolean approve) {
        return patch("/{id}?approved={approved}", ownerId,
                Map.of("id", bookingId, "approved", approve), null);
    }
}


