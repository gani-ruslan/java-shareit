package ru.practicum.shareit.booking.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.common.advice.GlobalErrorHandler;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@Import(GlobalErrorHandler.class)
class BookingControllerStateParamTest {

    @Autowired MockMvc mvc;
    @MockBean BookingClient client;

    @ParameterizedTest
    @ValueSource(strings = {"ALL","CURRENT","FUTURE","PAST","REJECTED","WAITING"})
    void listForBookers_validStates_200(String state) throws Exception {
        when(client.listForBookers(1L, state)).thenReturn(ResponseEntity.ok().body("[]"));

        mvc.perform(get("/bookings")
                        .param("state", state)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());

        verify(client).listForBookers(eq(1L), eq(state));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ALL","CURRENT","FUTURE","PAST","REJECTED","WAITING"})
    void listForOwner_validStates_200(String state) throws Exception {
        when(client.listForOwner(7L, state)).thenReturn(ResponseEntity.ok().body("[]"));

        mvc.perform(get("/bookings/owner")
                        .param("state", state)
                        .header("X-Sharer-User-Id", "7"))
                .andExpect(status().isOk());

        verify(client).listForOwner(eq(7L), eq(state));
    }

    @ParameterizedTest
    @ValueSource(strings = {"???", "foo"})
    void listForBookers_invalidState_400(String state) throws Exception {
        mvc.perform(get("/bookings")
                        .param("state", state)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(client);
    }
}
