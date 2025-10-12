package ru.practicum.shareit.comments.repository.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.comments.model.Comment;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"author"})
    List<Comment> findByItem_IdOrderByCreatedDesc(Long itemId);

    @EntityGraph(attributePaths = {"author"})
    List<Comment> findByItem_IdInOrderByItem_IdAscCreatedDesc(List<Long> itemIds);
}
