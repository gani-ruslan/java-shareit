package ru.practicum.shareit.user.web;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;

    @GetMapping
    public List<UserDto> findAll() {
        return mapper.toDto(userService.findAll());
    }

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable("userId") Long userId) {
        return mapper.toDto(userService.findById(userId));
    }

    @PostMapping
    public UserDto create(@RequestBody @Validated CreateUserRequest user) {
        return mapper.toDto(userService.create(mapper.toDomainCreate(user)));
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody @Validated UpdateUserRequest user,
                          @PathVariable("userId") Long userId) {
        return mapper.toDto(userService.update(userId, user));
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable("userId") Long userId) {
        userService.deleteById(userId);
    }
}
