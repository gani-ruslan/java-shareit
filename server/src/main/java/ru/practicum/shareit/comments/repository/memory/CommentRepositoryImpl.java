package ru.practicum.shareit.comments.repository.memory;

import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.comments.model.Comment;
import ru.practicum.shareit.comments.repository.CommentRepository;

@Repository
@Profile("memory")
public class CommentRepositoryImpl implements CommentRepository {
    @Override
    public Comment save(Comment comment) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Comment> findByItemId(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Comment> findByItemIds(List<Long> itemIds) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
