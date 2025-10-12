package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.common.advice.GlobalErrorHandler;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserPatchDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(GlobalErrorHandler.class)
@AutoConfigureJsonTesters
class UserControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @MockBean UserClient client;

    @Test
    void findAll_ok() throws Exception {
        when(client.findAll()).thenReturn(ResponseEntity.ok().body("[]"));

        mvc.perform(get("/users"))
                .andExpect(status().isOk());

        verify(client).findAll();
    }

    @Test
    void findById_ok() throws Exception {
        when(client.findById(1L)).thenReturn(ResponseEntity.ok().build());

        mvc.perform(get("/users/1"))
                .andExpect(status().isOk());

        verify(client).findById(1L);
    }

    @Test
    void findById_idZero_400() throws Exception {
        mvc.perform(get("/users/0"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(client);
    }

    @Test
    void findById_nonNumeric_400() throws Exception {
        mvc.perform(get("/users/abc"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(client);
    }

    @Test
    void save_ok() throws Exception {
        var dto = new UserCreateDto("Vasya", "mail@mail.com");
        when(client.save(any())).thenReturn(ResponseEntity.ok().build());

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(client).save(any());
    }

    @Test
    void save_badEmail_400() throws Exception {
        var dto = new UserCreateDto("Vasya", "bad-email");

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(client);
    }

    @Test
    void patch_ok() throws Exception {
        var dto = new UserPatchDto(null, "new@mail.com");
        when(client.patch(eq(2L), any())).thenReturn(ResponseEntity.ok().build());

        mvc.perform(patch("/users/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(client).patch(eq(2L), any());
    }

    @Test
    void patch_badEmail_400() throws Exception {
        var dto = new UserPatchDto(null, "bad-email");

        mvc.perform(patch("/users/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(client);
    }

    @Test
    void delete_ok() throws Exception {
        // метод клиента может быть void — просто проверяем вызов и статус
        mvc.perform(delete("/users/3"))
                .andExpect(status().isOk());
        verify(client).deleteById(3L);
    }
}
