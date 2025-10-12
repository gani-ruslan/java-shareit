package ru.practicum.shareit.request.repository.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.Request;

@Repository
public interface RequestJpaRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterIdOrderByCreatedDesc(Long requesterId);

    List<Request> findAllByRequesterIdNotOrderByCreatedDesc(Long requesterId);
}
