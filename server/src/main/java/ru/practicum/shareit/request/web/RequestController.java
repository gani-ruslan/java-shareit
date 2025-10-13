package ru.practicum.shareit.request.web;

import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestFullResponseDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.service.RequestService;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public RequestResponseDto create(
            @RequestHeader("X-Sharer-User-Id")Long userId,
            @RequestBody @Valid RequestCreateDto dto) {
        return requestService.addRequest(userId, dto);
    }

    @GetMapping
    public List<RequestFullResponseDto> findOwnRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.findOwnRequests(userId);
    }

    @GetMapping("/all")
    public List<RequestFullResponseDto> findOtherRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.findOtherRequests(userId);
    }

    @GetMapping("/{id}")
    public RequestFullResponseDto findById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable("id") Long id) {
        return requestService.findById(userId, id);
    }
}