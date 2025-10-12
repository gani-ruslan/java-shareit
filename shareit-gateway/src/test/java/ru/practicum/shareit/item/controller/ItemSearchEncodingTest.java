package ru.practicum.shareit.item.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.common.advice.GlobalErrorHandler;
import ru.practicum.shareit.item.client.ItemClient;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@Import(GlobalErrorHandler.class)
class ItemSearchEncodingTest {

    @Autowired MockMvc mvc;
    @MockBean ItemClient client;

    @Test
    void search_textWithSpacesAndCyrillic_isProxied() throws Exception {
        String q = "дрель 500Вт";
        when(client.search(q)).thenReturn(ResponseEntity.ok().body("[]"));

        mvc.perform(get("/items/search").param("text", q))
                .andExpect(status().isOk());

        verify(client).search(eq(q));
    }
}
