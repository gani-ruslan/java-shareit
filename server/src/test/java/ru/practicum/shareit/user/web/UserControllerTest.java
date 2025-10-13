package ru.practicum.shareit.user.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ActiveProfiles({"test"})
class UserControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockBean UserService userService;

    @Test
    void findAll_ok() throws Exception {
        when(userService.findAll()).thenReturn(List.of(new UserResponseDto(1L,"A","a@a.a")));

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(userService).findAll();
    }

    @Test
    void findById_ok() throws Exception {
        when(userService.findById(5L)).thenReturn(new UserResponseDto(5L,"B","b@b.b"));

        mvc.perform(get("/users/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("b@b.b"));
    }

    @Test
    void save_ok() throws Exception {
        var in = new UserCreateDto("C","c@c.c");
        when(userService.save(any())).thenReturn(new UserResponseDto(10L,"C","c@c.c"));

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(in)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void patch_ok() throws Exception {
        var in = new UserPatchDto("D", "d@d.d");
        when(userService.patch(eq(7L), any())).thenReturn(new UserResponseDto(7L,"D","d@d.d"));

        mvc.perform(patch("/users/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(in)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("D"));
    }

    @Test
    void delete_ok() throws Exception {
        mvc.perform(delete("/users/9"))
                .andExpect(status().isOk());
        verify(userService).deleteById(9L);
    }
}
