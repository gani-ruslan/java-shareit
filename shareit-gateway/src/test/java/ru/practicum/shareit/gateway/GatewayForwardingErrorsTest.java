package ru.practicum.shareit.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.common.advice.GlobalErrorHandler;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.client.ItemClient;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class) // любой контроллер gateway подойдёт; здесь — items
@Import(GlobalErrorHandler.class)
class GatewayForwardingErrorsTest {

    @Autowired MockMvc mvc;
    @MockBean ItemClient client;

    @Test
    void server404_isForwardedAsIs() throws Exception {
        String body = "{\"error\":\"NOT_FOUND\",\"message\":\"item not found\"}";
        when(client.findById(null, 777L)).thenReturn(ResponseEntity.status(404).body(body));

        mvc.perform(get("/items/777"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(body, true)); // JSONAssert уже добавлен
    }
}
