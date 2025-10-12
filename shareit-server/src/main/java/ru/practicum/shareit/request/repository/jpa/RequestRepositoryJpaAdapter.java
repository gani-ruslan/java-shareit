package ru.practicum.shareit.request.repository.jpa;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;

@Repository
@Profile("jpa")
@RequiredArgsConstructor
public class RequestRepositoryJpaAdapter implements RequestRepository{

    private final RequestJpaRepository jpa;

    @Override
    public List<Request> findOthers(Long requesterId) {
        return jpa.findAllByRequesterIdNotOrderByCreatedDesc(requesterId);
    }

    @Override
    public List<Request> findOwn(Long requesterId) {
        return jpa.findAllByRequesterIdOrderByCreatedDesc(requesterId);
    }

    @Override
    public Request save(Request request) {
        return jpa.save(request);
    }

    @Override
    public Optional<Request> findById(Long id) {
        return jpa.findById(id);
    }
}
