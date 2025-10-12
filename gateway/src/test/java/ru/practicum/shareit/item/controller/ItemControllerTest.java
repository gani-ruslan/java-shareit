package ru.practicum.shareit.item.controller;

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
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.CommentCreateDto;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@Import(GlobalErrorHandler.class)
class ItemControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockBean ItemClient client;

    @Test
    void listByOwner_ok() throws Exception {
        when(client.findByOwnerId(10L)).thenReturn(ResponseEntity.ok().body("[]"));

        mvc.perform(get("/items").header("X-Sharer-User-Id", "10"))
                .andExpect(status().isOk());

        verify(client).findByOwnerId(10L);
    }

    @Test
    void listByOwner_missingHeader_400() throws Exception {
        mvc.perform(get("/items"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(client);
    }

    @Test
    void getById_ok_headerOptional() throws Exception {
        when(client.findById(null, 5L)).thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/items/5"))
                .andExpect(status().isOk());

        verify(client).findById(null, 5L);
    }

    @Test
    void getById_idZero_400() throws Exception {
        mvc.perform(get("/items/0"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(client);
    }

    @Test
    void search_blank_returnsEmpty_andClientNotCalled() throws Exception {
        mvc.perform(get("/items/search?text="))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verifyNoInteractions(client);
    }

    @Test
    void search_nonBlank_proxiesToClient() throws Exception {
        when(client.search("дрель 500Вт")).thenReturn(ResponseEntity.ok().body("[]"));

        mvc.perform(get("/items/search")
                        .param("text", "дрель 500Вт"))
                .andExpect(status().isOk());

        verify(client).search("дрель 500Вт");
    }

    @Test
    void addItem_ok() throws Exception {
        var dto = new ItemCreateDto("Дрель", "ударная", true, null);
        when(client.addItem(eq(1L), any())).thenReturn(ResponseEntity.ok().build());

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(client).addItem(eq(1L), any());
    }

    @Test
    void addItem_missingHeader_400() throws Exception {
        var dto = new ItemCreateDto("Дрель", "ударная", true, null);

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(client);
    }

    @Test
    void addComment_ok() throws Exception {
        var dto = new CommentCreateDto("классная вещь");
        when(client.addComment(2L, 9L, dto)).thenReturn(ResponseEntity.ok().build());

        mvc.perform(post("/items/9/comment")
                        .header("X-Sharer-User-Id", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(client).addComment(2L, 9L, dto);
    }

    @Test
    void addComment_blankText_400() throws Exception {
        var dto = new CommentCreateDto(" ");

        mvc.perform(post("/items/9/comment")
                        .header("X-Sharer-User-Id", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(client);
    }

    @Test
    void patch_ok() throws Exception {

        var dto = new ItemPatchDto(null, "upd", null, null);
        when(client.patch(3L, 7L, dto)).thenReturn(ResponseEntity.ok().build());

        mvc.perform(patch("/items/7")
                        .header("X-Sharer-User-Id", "3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(client).patch(3L, 7L, dto);
    }

    @Test
    void patch_missingHeader_400() throws Exception {
        var dto = new ItemPatchDto(null, "upd", null, null);

        mvc.perform(patch("/items/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(client);
    }
}
