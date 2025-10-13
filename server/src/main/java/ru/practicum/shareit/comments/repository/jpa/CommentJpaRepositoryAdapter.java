package ru.practicum.shareit.comments.repository.jpa;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.comments.model.Comment;
import ru.practicum.shareit.comments.repository.CommentRepository;

@Repository
@RequiredArgsConstructor
public class CommentJpaRepositoryAdapter implements CommentRepository {

    private final CommentJpaRepository jpa;

    @Override
    public Comment save(Comment comment) {
        return jpa.save(comment);
    }

    @Override
    public List<Comment> findByItemId(Long id) {
        return jpa.findByItem_IdOrderByCreatedDesc(id);
    }

    @Override
    public List<Comment> findByItemIds(List<Long> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) return List.of();
        return jpa.findByItem_IdInOrderByItem_IdAscCreatedDesc(itemIds);
    }
}
