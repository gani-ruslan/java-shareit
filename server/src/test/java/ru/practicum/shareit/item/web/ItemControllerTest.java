package ru.practicum.shareit.item.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.comments.dto.CommentCreateDto;
import ru.practicum.shareit.comments.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.comments.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockBean ItemService itemService;
    @MockBean CommentService commentService;

    @Test
    void findByOwner_ok() throws Exception {
        when(itemService.findByOwnerId(1L)).thenReturn(List.of(
                new ItemResponseDto(10L,"A","d",true,1L,null,null,null,List.of())
        ));

        mvc.perform(get("/items").header("X-Sharer-User-Id","1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10));
    }

    @Test
    void findById_ok() throws Exception {
        when(itemService.findById(null, 5L)).thenReturn(
                new ItemResponseDto(5L,"I","d",true,1L,null,null,null,List.of())
        );

        mvc.perform(get("/items/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("I"));
    }

    @Test
    void search_ok() throws Exception {
        when(itemService.search("дрель")).thenReturn(List.of(
                new ItemResponseDto(2L,"Дрель","",true,1L,null,null,null,List.of())
        ));

        mvc.perform(get("/items/search").param("text","дрель"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    void addItem_ok() throws Exception {
        var in = new ItemCreateDto("X","desc",true,null);
        when(itemService.addItem(3L, in)).thenReturn(
                new ItemResponseDto(100L,"X","desc",true,3L,null,null,null,List.of())
        );

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id","3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(in)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerId").value(3));
    }

    @Test
    void addComment_ok() throws Exception {
        var in = new CommentCreateDto("норм");
        when(commentService.addComment(9L, 3L, in))
                .thenReturn(new CommentResponseDto(1L,"норм", 9L, LocalDateTime.now(), "who"));

        mvc.perform(post("/items/9/comment")
                        .header("X-Sharer-User-Id","3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(in)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void patch_ok() throws Exception {
        var in = new ItemPatchDto(null,"upd",null,null);
        when(itemService.patch(7L, 8L, in)).thenReturn(
                new ItemResponseDto(8L,"I","upd",true,7L,null,null,null,List.of())
        );

        mvc.perform(patch("/items/8")
                        .header("X-Sharer-User-Id","7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(in)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("upd"));
    }
}
