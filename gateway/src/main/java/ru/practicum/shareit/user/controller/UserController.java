package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserPatchDto;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> findAll() {
        log.info("Get all users information");
        return userClient.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findById(@PathVariable("userId") @Positive long userId) {
        log.info("Get user with id={} information", userId);
        return userClient.findById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid UserCreateDto user) {
        log.info("Create user with data {}", user);
        return userClient.save(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> patch(@RequestBody @Valid UserPatchDto user,
                                        @PathVariable("userId") @Positive long userId) {
        log.info("Update user {} with id={}", user, userId);
        return userClient.patch(userId, user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteById(@PathVariable("userId") @Positive long userId) {
        log.info("Delete user with id={}", userId);
        return userClient.deleteById(userId);
    }
}
