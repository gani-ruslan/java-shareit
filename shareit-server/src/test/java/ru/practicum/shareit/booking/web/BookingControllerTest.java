package ru.practicum.shareit.booking.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.advice.GlobalErrorHandler;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@Import(GlobalErrorHandler.class)
@ActiveProfiles("test")
class BookingControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockBean BookingService bookingService;

    @Test
    void getById_ok() throws Exception {
        when(bookingService.findById(2L, 5L))
                .thenReturn(new BookingResponseDto(5L, null, null, null, null, null));

        mvc.perform(get("/bookings/5")
                        .header("X-Sharer-User-Id", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void listForBookers_ok() throws Exception {
        when(bookingService.listForBooker(1L, "ALL")).thenReturn(List.of());

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void listForOwner_ok() throws Exception {
        when(bookingService.listForOwner(7L, "CURRENT")).thenReturn(List.of());

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "7")
                        .param("state", "CURRENT"))
                .andExpect(status().isOk());
    }

    @Test
    void save_ok() throws Exception {
        var dto = new BookingCreateDto(
                10L,
                LocalDateTime.of(2030, 1, 1, 10, 0),
                LocalDateTime.of(2030, 1, 2, 10, 0)
        );

        when(bookingService.save(eq(3L), any(BookingCreateDto.class)))
                .thenReturn(new BookingResponseDto(100L, null, null, null, null, null));

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100));
    }

    @Test
    void approve_ok() throws Exception {
        when(bookingService.approve(4L, 9L, true))
                .thenReturn(new BookingResponseDto(9L, null, null, null, null, null));

        mvc.perform(patch("/bookings/9")
                        .header("X-Sharer-User-Id", "4")
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(9));
    }
}
