package ru.practicum.shareit.comments.repository;

import java.util.List;
import ru.practicum.shareit.comments.model.Comment;

public interface CommentRepository {
    Comment save(Comment comment);

    List<Comment> findByItemId(Long id);

    List<Comment> findByItemIds(List<Long> itemIds);
}
