package ru.practicum.shareit.request.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.request.client.RequestClient;
import ru.practicum.shareit.request.controller.RequestController;

@WebMvcTest(RequestController.class)
class RequestControllerValidationTest {
    @Autowired MockMvc mvc;
    @MockBean RequestClient requestClient;

    @Test
    void findOwn_missingHeader_returns400() throws Exception {
        mvc.perform(get("/requests"))
                .andExpect(status().isBadRequest());
    }
}
