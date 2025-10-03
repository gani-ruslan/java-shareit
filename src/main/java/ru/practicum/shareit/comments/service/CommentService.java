package ru.practicum.shareit.comments.service;

import static ru.practicum.shareit.common.advice.ErrorHelpers.badRequest;
import static ru.practicum.shareit.common.advice.ErrorHelpers.notFound;

import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comments.dto.CommentCreateDto;
import ru.practicum.shareit.comments.dto.CommentResponseDto;
import ru.practicum.shareit.comments.mapper.CommentMapper;
import ru.practicum.shareit.comments.model.Comment;
import ru.practicum.shareit.comments.repository.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final CommentMapper mapper;
    private final Clock clock;

    @Transactional
    public CommentResponseDto addComment(Long itemId, Long authorId, CommentCreateDto dto) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> notFound("User", authorId));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> notFound("Item", itemId));

        LocalDateTime now = LocalDateTime.now(clock);

        boolean isOwner = item.getOwner().getId().equals(author.getId());
        boolean hadPastApproved = isUserHadApprovedPastBooking(author.getId(), item.getId(), now);

        if (!(isOwner || hadPastApproved)) {
            throw badRequest("Нельзя комментировать до завершения или без завершенного бронирования");
        }

        Comment comment = mapper.toEntity(dto, author, item);
        comment.setCreated(now);

        Comment savedComment = commentRepository.save(comment);
        return mapper.toResponse(savedComment);
    }

    private boolean isUserHadApprovedPastBooking(Long bookerId, Long itemId, LocalDateTime ts) {
        return bookingRepository.getLastApprovedBooking(bookerId, itemId, BookingStatus.APPROVED)
                .map(booking -> !booking.getEndDate().isAfter(ts))
                .orElse(false);
    }

}
