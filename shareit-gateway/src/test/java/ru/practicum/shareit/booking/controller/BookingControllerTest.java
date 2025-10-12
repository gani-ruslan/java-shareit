package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.common.advice.GlobalErrorHandler;

import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@Import(GlobalErrorHandler.class)
class BookingControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockBean BookingClient client;

    @Test
    void getById_ok() throws Exception {
        when(client.findById(2L, 5L)).thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/bookings/5")
                        .header("X-Sharer-User-Id", "2"))
                .andExpect(status().isOk());

        verify(client).findById(2L, 5L);
    }

    @Test
    void getById_missingHeader_400() throws Exception {
        mvc.perform(get("/bookings/5"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(client);
    }

    @Test
    void listForBookers_stateOk() throws Exception {
        when(client.listForBookers(1L, "ALL")).thenReturn(ResponseEntity.ok().body("[]"));

        mvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());

        verify(client).listForBookers(1L, "ALL");
    }

    @Test
    void listForBookers_unknownState_400() throws Exception {
        mvc.perform(get("/bookings")
                        .param("state", "???")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(client);
    }

    @Test
    void listForOwner_stateOk() throws Exception {
        when(client.listForOwner(7L, "CURRENT")).thenReturn(ResponseEntity.ok().body("[]"));

        mvc.perform(get("/bookings/owner")
                        .param("state", "CURRENT")
                        .header("X-Sharer-User-Id", "7"))
                .andExpect(status().isOk());

        verify(client).listForOwner(7L, "CURRENT");
    }

    @Test
    void save_ok() throws Exception {
        var dto = new BookingCreateDto(
                10L,
                LocalDateTime.of(2030,1,1,10,0),
                LocalDateTime.of(2030,1,2,10,0)
        );
        when(client.save(3L, dto)).thenReturn(ResponseEntity.ok().build());

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(client).save(3L, dto);
    }

    @Test
    void save_missingBodyField_400() throws Exception {
        // itemId = null → 400
        var broken = Map.of(
                "start", "2030-01-01T10:00:00",
                "end",   "2030-01-02T10:00:00"
        );

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(broken)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(client);
    }

    @Test
    void approve_ok() throws Exception {
        when(client.approve(4L, 9L, true)).thenReturn(ResponseEntity.ok().build());

        mvc.perform(patch("/bookings/9")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", "4"))
                .andExpect(status().isOk());

        verify(client).approve(4L, 9L, true);
    }

    @Test
    void approve_missingQueryParam_400() throws Exception {
        mvc.perform(patch("/bookings/9")
                        .header("X-Sharer-User-Id", "4"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(client);
    }
}
