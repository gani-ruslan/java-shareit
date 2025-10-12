package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"test", "jpa"})
@AutoConfigureTestDatabase
@Transactional
class ItemServiceIT {

    @Autowired private ItemService itemService;
    @Autowired private UserRepository userRepository;
    @Autowired private RequestRepository requestRepository;

    private User owner = new User();
    private Request req;

    @BeforeEach
    void setUp() {
        owner.setName("Owner");
        owner.setEmail("o@o.o");
        owner = userRepository.save(owner);

        req = new Request();
        req.setDescription("Нужна дрель");
        req.setRequester(owner);
        req.setCreated(LocalDateTime.now());
        req = requestRepository.save(req);
    }

    @Test
    void addItem_withRequestId_linksItemToRequest() {
        ItemCreateDto dto = new ItemCreateDto("Дрель", "ударная", true, req.getId());
        ItemResponseDto saved = itemService.addItem(owner.getId(), dto);

        assertThat(saved.id()).isNotNull();
        assertThat(saved.requestId()).isEqualTo(req.getId());
    }
}
