package ru.practicum.shareit.request.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestFullResponseDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

@Component
public class RequestMapper {
    /**
     * In.
     */
    public Request toEntity(RequestCreateDto dto, User requester, LocalDateTime createdAt) {
        Request r = new Request();
        r.setDescription(dto.description());
        r.setRequester(requester);
        r.setCreated(createdAt);
        return r;
    }

    /**
     * Out.
     */
    public RequestResponseDto toSimpleResponse(Request r) {
        return new RequestResponseDto(
                r.getId(),
                r.getDescription(),
                r.getRequester() != null ? r.getRequester().getId() : r.getRequesterId(),
                r.getCreated()
        );
    }

    public List<RequestFullResponseDto> toFullResponse(List<Request> requests,
                                                       Map<Long, List<ShortItemDto>> itemsByRequestId) {
        return requests.stream()
                .map(r -> new RequestFullResponseDto(
                        r.getId(),
                        r.getDescription(),
                        r.getRequester() != null ? r.getRequester().getId() : r.getRequesterId(),
                        r.getCreated(),
                        itemsByRequestId.getOrDefault(r.getId(), java.util.List.of())
                ))
                .toList();
    }

    public RequestFullResponseDto toFullResponse(Request r,
                                                 List<ShortItemDto> items) {
        return new RequestFullResponseDto(
                r.getId(),
                r.getDescription(),
                r.getRequester() != null ? r.getRequester().getId() : r.getRequesterId(),
                r.getCreated(),
                items
        );
    }
}
