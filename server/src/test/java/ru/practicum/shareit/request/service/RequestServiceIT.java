package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestFullResponseDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"test", "jpa"})
@AutoConfigureTestDatabase
@Transactional
class RequestServiceIT {

    @Autowired private RequestService requestService;
    @Autowired private UserRepository userRepository;
    @Autowired private RequestRepository requestRepository;
    @Autowired private ItemRepository itemRepository;

    private User u1 = new User();
    private User u2 = new User();
    private Request r1 = new Request();
    private Request r2 = new Request();

    @BeforeEach
    void setUp() {
        u1.setName("Al");
        u1.setEmail("a@a.a");
        u2.setName("Bob");
        u2.setEmail("b@b.b");

        u1 = userRepository.save(u1);
        u2 = userRepository.save(u2);

        r1.setDescription("Нужна дрель");
        r1.setRequester(u1); // <-- ПРАВИЛЬНЫЙ сеттер
        r1.setCreated(LocalDateTime.of(2030,1,1,10,0));
        r1 = requestRepository.save(r1);

        r2.setDescription("Нужна лестница");
        r2.setRequester(u1); // <-- ПРАВИЛЬНЫЙ сеттер
        r2.setCreated(LocalDateTime.of(2030,1,2,10,0));
        r2 = requestRepository.save(r2);

        Item i = new Item();
        i.setName("Дрель-ударная");
        i.setDescription("500Вт");
        i.setAvailable(true);
        i.setOwner(u2);
        i.setRequest(r2);
        itemRepository.save(i);
    }

    @Test
    void create_shouldPersist() {
        RequestCreateDto dto = new RequestCreateDto("Нужен перфоратор");
        RequestResponseDto saved = requestService.addRequest(u1.getId(), dto);

        assertThat(saved.id()).isNotNull();
        assertThat(saved.description()).isEqualTo("Нужен перфоратор");
        assertThat(saved.requesterId()).isEqualTo(u1.getId());
        assertThat(saved.created()).isNotNull();
    }

    @Test
    void findOwnRequests_sortedAndWithItems() {
        List<RequestFullResponseDto> list = requestService.findOwnRequests(u1.getId());
        assertThat(list).hasSize(2);

        assertThat(list.get(0).description()).isEqualTo("Нужна лестница");
        assertThat(list.get(0).items()).hasSize(1);
        assertThat(list.get(1).description()).isEqualTo("Нужна дрель");
        assertThat(list.get(1).items()).isEmpty();
    }

    @Test
    void findOthersRequests_returnsRequestsOfOtherUsers() {
        List<RequestFullResponseDto> others = requestService.findOtherRequests(u2.getId());
        assertThat(others).hasSize(2);
    }

    @Test
    void findById_returnsRequestWithItems() {
        RequestFullResponseDto dto = requestService.findById(u2.getId(), r2.getId());
        assertThat(dto.items()).hasSize(1);
        assertThat(dto.items().getFirst().name()).contains("Дрель");
    }
}
