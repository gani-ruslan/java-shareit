package ru.practicum.shareit.item.client;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.common.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> findByOwnerId(long ownerId) {
        return get("", ownerId);
    }

    public ResponseEntity<Object> findById(Long viewerId, long itemId) {
        return get("/" + itemId, viewerId);
    }

    public ResponseEntity<Object> search(String query) {
        return get("/search?text={text}", null, Map.of("text", query));
    }

    public ResponseEntity<Object> addItem(long ownerId, ItemCreateDto dto) {
        return post("", ownerId, dto);
    }

    public ResponseEntity<Object> addComment(long authorId, long itemId, CommentCreateDto dto) {
        return post("/" + itemId + "/comment", authorId, dto);
    }

    public ResponseEntity<Object> patch(long ownerId, long itemId, ItemPatchDto dto) {
        return patch("/" + itemId, ownerId, dto);
    }
}