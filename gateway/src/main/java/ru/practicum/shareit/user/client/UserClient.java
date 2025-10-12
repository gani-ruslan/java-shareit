package ru.practicum.shareit.user.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.common.client.BaseClient;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserPatchDto;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> findAll() {
        return get("");
    }

    public ResponseEntity<Object> findById(Long id) {
        return get("/" + id);
    }

    public ResponseEntity<Object> save(UserCreateDto dto) {
        return post("", dto);
    }

    public ResponseEntity<Object> patch(Long id, UserPatchDto dto) {
        return patch("/" + id, dto);
    }

    public ResponseEntity<Object> deleteById(Long id) {
        return delete("/" + id);
    }
}
