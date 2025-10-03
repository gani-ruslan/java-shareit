package ru.practicum.shareit.user.web;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserResponseDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserResponseDto findById(@PathVariable("userId") @Positive Long userId) {
        return userService.findById(userId);
    }

    @PostMapping
    public UserResponseDto save(@RequestBody @Valid UserCreateDto user) {
        return userService.save(user);
    }

    @PatchMapping("/{userId}")
    public UserResponseDto patch(@RequestBody @Valid UserPatchDto user,
                                  @PathVariable("userId") @Positive Long userId) {
        return userService.patch(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable("userId") @Positive Long userId) {
        userService.deleteById(userId);
    }
}
