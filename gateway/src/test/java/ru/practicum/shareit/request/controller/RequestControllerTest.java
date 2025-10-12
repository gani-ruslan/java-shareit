package ru.practicum.shareit.request.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.common.advice.GlobalErrorHandler;
import ru.practicum.shareit.request.client.RequestClient;
import ru.practicum.shareit.request.dto.RequestCreateDto;

@WebMvcTest(RequestController.class)
@Import(GlobalErrorHandler.class)
class RequestControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockBean RequestClient client;

    @Test
    void create_ok() throws Exception {
        var dto = new RequestCreateDto("Нужна дрель");
        when(client.addRequest(1L, dto)).thenReturn(ResponseEntity.ok().build());

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(client).addRequest(1L, dto);
    }

    @Test
    void create_missingHeader_400() throws Exception {
        var dto = new RequestCreateDto("Нужна дрель");

        mvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(client);
    }

    @Test
    void create_blankDescription_400() throws Exception {
        var dto = new RequestCreateDto(" ");

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(client);
    }

    @Test
    void findOwn_ok() throws Exception {
        when(client.findOwnRequests(2L)).thenReturn(ResponseEntity.ok().body("[]"));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "2"))
                .andExpect(status().isOk());

        verify(client).findOwnRequests(2L);
    }

    @Test
    void findAll_ok() throws Exception {
        when(client.findOtherRequests(3L)).thenReturn(ResponseEntity.ok().body("[]"));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "3"))
                .andExpect(status().isOk());

        verify(client).findOtherRequests(3L);
    }

    @Test
    void findById_ok() throws Exception {
        when(client.findById(4L, 10L)).thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/requests/10")
                        .header("X-Sharer-User-Id", "4"))
                .andExpect(status().isOk());

        verify(client).findById(4L, 10L);
    }

    @Test
    void findById_badIds_400() throws Exception {
        mvc.perform(get("/requests/0").header("X-Sharer-User-Id", "0"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(client);
    }
}
