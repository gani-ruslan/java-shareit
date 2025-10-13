package ru.practicum.shareit.request.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestFullResponseDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RequestController.class)
@ActiveProfiles("test")
class RequestControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockBean RequestService requestService;

    @Test
    void create_ok() throws Exception {
        var in = new RequestCreateDto("нужна дрель");

        when(requestService.addRequest(eq(1L), any(RequestCreateDto.class)))
                .thenReturn(new RequestResponseDto(10L, "нужна дрель", 1L, LocalDateTime.now()));

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(in)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void findOwn_ok() throws Exception {
        when(requestService.findOwnRequests(2L))
                .thenReturn(List.of(
                        new RequestFullResponseDto(
                                1L,
                                "A",
                                2L,
                                LocalDateTime.now(),
                                List.<ShortItemDto>of()
                        )));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void findAll_ok() throws Exception {
        when(requestService.findOtherRequests(3L)).thenReturn(List.of());

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "3"))
                .andExpect(status().isOk());
    }

    @Test
    void findById_ok() throws Exception {
        when(requestService.findById(4L, 99L))
                .thenReturn(new RequestFullResponseDto(
                        99L,
                        "X",
                        4L,
                        LocalDateTime.now(),
                        List.<ShortItemDto>of()
                ));

        mvc.perform(get("/requests/99")
                        .header("X-Sharer-User-Id", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(99));
    }
}
