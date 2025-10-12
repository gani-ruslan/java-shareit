package ru.practicum.shareit.booking.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.controller.BookingController;

@WebMvcTest(BookingController.class)
class BookingControllerValidationTest {
    @Autowired MockMvc mvc;
    @MockBean BookingClient bookingClient;

    @Test
    void listForOwner_invalidState_returns400() throws Exception {
        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "UNSUPPORTED"))
                .andExpect(status().isBadRequest());
    }
}
