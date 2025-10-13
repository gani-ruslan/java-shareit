package ru.practicum.shareit.user.service;

import static ru.practicum.shareit.common.advice.ErrorHelpers.conflict;
import static ru.practicum.shareit.common.advice.ErrorHelpers.notFound;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.jpa.UserJpaRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserJpaRepository userRepository;
    private final UserMapper mapper;

    @Transactional
    public UserResponseDto save(UserCreateDto dto) {
        if (userRepository.existsByEmail(dto.email()))
            throw conflict(dto.email());

        User user = mapper.toEntity(dto);
        User savedUser = userRepository.save(user);
        return mapper.toResponse(savedUser);
    }

    @Transactional
    public UserResponseDto patch(Long id, UserPatchDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> notFound("User", id));

        if (user.getEmail() != null && !user.getEmail().equals(dto.email())) {
            if (userRepository.existsByEmail(dto.email())) {
                throw conflict(dto.email());
            }
        }
        mapper.patchEntity(user, dto);
        return mapper.toResponse(user);
    }

    public List<UserResponseDto> findAll() {
        return mapper.toResponse(
                userRepository.findAll()
        );
    }

    public UserResponseDto findById(Long id) {
        return mapper.toResponse(
                userRepository.findById(id)
                .orElseThrow(() -> notFound("User", id))
        );
    }

    @Transactional
    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> notFound("User", id));
        userRepository.delete(user);
    }
}