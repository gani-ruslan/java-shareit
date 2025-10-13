package ru.practicum.shareit.request.service;

import static ru.practicum.shareit.common.advice.ErrorHelpers.notFound;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestFullResponseDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestService {
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RequestMapper mapper;
    private final ItemMapper itemMapper;
    private final Clock clock;

    public RequestFullResponseDto findById(Long userId, Long id) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> notFound("User", userId));
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> notFound("Request", id));

        List<ShortItemDto> items = itemRepository.findAllByRequestId(request.getId())
                .stream()
                .map(itemMapper::toShort)
                .toList();
        return mapper.toFullResponse(request, items);
    }

    @Transactional
    public RequestResponseDto addRequest(Long userId, RequestCreateDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> notFound("User", userId));

        Request request = mapper.toEntity(dto, user, LocalDateTime.now(clock));

        Request savedRequest = requestRepository.save(request);
        return mapper.toSimpleResponse(savedRequest);
    }

    public List<RequestFullResponseDto> findOwnRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> notFound("User", userId));

        return buildResponseDto(requestRepository.findOwn(user.getId()));
    }

    public List<RequestFullResponseDto> findOtherRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> notFound("User", userId));
        return buildResponseDto(requestRepository.findOthers(user.getId()));
    }

    private List<RequestFullResponseDto> buildResponseDto(List<Request> requests) {
        if (requests.isEmpty()) {
            return List.of();
        }

        List<Long> requestsIdList = requests.stream()
                .map(Request::getId)
                .toList();

        Map<Long, List<ShortItemDto>> itemsByRequests = itemRepository.findAllByRequestIdIn(requestsIdList)
                .stream()
                .filter(i -> i.getRequest().getId() != null)
                .collect(Collectors.groupingBy(
                        i -> i.getRequest().getId(),
                        Collectors.mapping(
                                itemMapper::toShort,
                                Collectors.toList()
                        )
                ));

        return mapper.toFullResponse(requests, itemsByRequests);
    }
}
