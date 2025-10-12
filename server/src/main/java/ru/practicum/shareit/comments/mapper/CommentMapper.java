package ru.practicum.shareit.comments.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.comments.dto.CommentCreateDto;
import ru.practicum.shareit.comments.dto.CommentResponseDto;
import ru.practicum.shareit.comments.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Component
public class CommentMapper {

    /**
     * In.
     */
    public Comment toEntity(CommentCreateDto dto) {
        Comment c = new Comment();
        c.setText(dto.text());
        return c;
    }

    public Comment toEntity(CommentCreateDto dto, User author, Item item) {
        Comment c = new Comment();
        c.setText(dto.text());
        c.setAuthor(author);
        c.setItem(item);
        return c;
    }

    /**
     * Out.
     */
    public List<CommentResponseDto> toResponse(List<Comment> comments) {
        return comments.stream()
                .map(this::toResponse)
                .toList();
    }

    public CommentResponseDto toResponse(Comment c) {
        return new CommentResponseDto(
                c.getId(),
                c.getText(),
                c.getItem().getId(),
                c.getCreated(),
                c.getAuthor().getName()
        );
    }
}