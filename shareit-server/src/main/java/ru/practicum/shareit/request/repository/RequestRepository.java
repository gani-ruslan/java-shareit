package ru.practicum.shareit.request.repository;

import java.util.List;
import java.util.Optional;
import ru.practicum.shareit.request.model.Request;

public interface RequestRepository {

    List<Request> findOthers(Long requesterId);

    List<Request> findOwn(Long requesterId);

    Request save(Request request);

    Optional<Request> findById(Long id);
}