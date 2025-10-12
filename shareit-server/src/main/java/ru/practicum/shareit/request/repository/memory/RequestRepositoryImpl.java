package ru.practicum.shareit.request.repository.memory;

import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;

@Repository
@Profile("memory")
public class RequestRepositoryImpl implements RequestRepository {

    @Override
    public List<Request> findOthers(Long requesterId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Request> findOwn(Long requesterId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Request save(Request request) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Request> findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
