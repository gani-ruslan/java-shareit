package ru.practicum.shareit.item.service;

import static ru.practicum.shareit.common.advice.ErrorHelpers.forbidden;
import static ru.practicum.shareit.common.advice.ErrorHelpers.notFound;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.Clock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comments.dto.CommentResponseDto;
import ru.practicum.shareit.comments.mapper.CommentMapper;
import ru.practicum.shareit.comments.model.Comment;
import ru.practicum.shareit.comments.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ItemMapper mapper;
    private final Clock clock;

    @Transactional
    public ItemResponseDto addItem(Long ownerId, ItemCreateDto dto) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> notFound("User", ownerId));

        Item item = mapper.toEntity(dto);
        item.setOwner(owner);

        Item savedItem = itemRepository.save(item);
        return mapper.toSimpleResponse(savedItem);
    }

    @Transactional
    public ItemResponseDto patch(Long ownerId, Long id, ItemPatchDto dto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> notFound("Item", id));
        if (!item.getOwner().getId().equals(ownerId)) throw forbidden("Редактирование запрещено");
        mapper.patchEntity(item, dto);
        return mapper.toSimpleResponse(item);
    }

    public ItemResponseDto findById(Long viewerId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> notFound("Item", itemId));

        return createRespondDto(List.of(item), viewerId).getFirst();
    }

    public List<ItemResponseDto> findByOwnerId(Long ownerId) {
        List<Item> items = itemRepository.findByOwner_IdOrderByIdAsc(ownerId);

        if (items.isEmpty()) {
            return List.of();
        } else {
            return createRespondDto(items, ownerId);
        }
    }

    public List<ItemResponseDto> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }

        List<Item> items = itemRepository.search(query);

        if (items.isEmpty()) {
            return List.of();
        } else {
            return createRespondDto(items, null);
        }
    }

    @Transactional
    public void deleteById(Long ownerId, Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> notFound("Item", id));
        if (!item.getOwner().getId().equals(ownerId)) {
            throw forbidden("Удаление запрещено.");
        }
        itemRepository.delete(item);
    }

    private List<ItemResponseDto> createRespondDto(List<Item> items, Long viewerId) {

        if (items.isEmpty()) {
            return List.of();
        }

        List<Long> itemIds = items.stream()
                .map(Item::getId)
                .toList();

        // Comments batch request and processing -> Map<itemId, List<CommentResponseDto>>
        List<Comment> allComments = commentRepository.findByItemIds(itemIds);

        Map<Long, List<CommentResponseDto>> commentsByItem = new HashMap<>();
        for (Comment c : allComments) {
            commentsByItem
                    .computeIfAbsent(c.getItem().getId(), k -> new ArrayList<>())
                    .add(commentMapper.toResponse(c));
        }

        // Last/Next if viewerId available, showing only for owner
        Map<Long, ShortBookingDto> nextTmp = new HashMap<>();
        Map<Long, ShortBookingDto> lastTmp = new HashMap<>();

        if (viewerId != null) {
            LocalDateTime now = LocalDateTime.now(clock);

            // last
            for (Booking b : bookingRepository.findLastApprovedForItems(itemIds, now)) {
                Long itemId = b.getItem().getId();
                if (b.getItem().getOwner().getId().equals(viewerId)) {
                    lastTmp.putIfAbsent(
                            itemId,
                            new ShortBookingDto(b.getId(), b.getBooker().getId())
                    );
                }
            }

            // next
            for (Booking b : bookingRepository.findNextApprovedForItems(itemIds, now)) {
                Long itemId = b.getItem().getId();
                if (b.getItem().getOwner().getId().equals(viewerId)) {
                    nextTmp.putIfAbsent(
                            itemId,
                            new ShortBookingDto(b.getId(), b.getBooker().getId())
                    );
                }
            }
        }

        final Map<Long, ShortBookingDto> lastByItem = lastTmp;
        final Map<Long, ShortBookingDto> nextByItem = nextTmp;

        // Create DTO
        return items.stream()
                .map(item -> mapper.toFullResponse(
                        item,
                        lastByItem.get(item.getId()),
                        nextByItem.get(item.getId()),
                        commentsByItem.getOrDefault(item.getId(), List.of())
                ))
                .toList();
    }
}