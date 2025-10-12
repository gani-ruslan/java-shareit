package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.client.RequestClient;
import ru.practicum.shareit.request.dto.RequestCreateDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader("X-Sharer-User-Id") @Positive long userId,
            @RequestBody @Valid RequestCreateDto dto) {
        log.info("Creating request from user with id={} request {}", userId, dto);
        return requestClient.addRequest(userId, dto);
    }

    @GetMapping
    public ResponseEntity<Object> findOwnRequests(
            @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        log.info("Find own request for user with id={}", userId);
        return requestClient.findOwnRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findOtherRequests(
            @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        log.info("Find others request user with id={}", userId);
        return requestClient.findOtherRequests(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(
            @RequestHeader("X-Sharer-User-Id") @Positive long userId,
            @PathVariable @Positive Long id) {
        log.info("Find request from user with id={} for item id={}", userId, id);
        return requestClient.findById(userId, id);
    }
}
