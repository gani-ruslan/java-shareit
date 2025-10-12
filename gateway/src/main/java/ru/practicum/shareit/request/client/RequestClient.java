package ru.practicum.shareit.request.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.common.client.BaseClient;
import ru.practicum.shareit.request.dto.RequestCreateDto;

@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> addRequest(long userId, RequestCreateDto dto) {
        return post("", userId, dto);
    }

    public ResponseEntity<Object> findOwnRequests(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> findOtherRequests(long userId) {
        return get("/all", userId);
    }

    public ResponseEntity<Object> findById(long userId, long id) {
        return get("/" + id, userId);
    }
}
