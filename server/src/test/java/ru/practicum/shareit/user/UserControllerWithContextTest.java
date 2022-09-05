package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerWithContextTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserDtoMapper userDtoMapper;

    UserDto userDto = UserDto.builder()
            .id(1L)
            .name("username")
            .email("email@ya.ru")
            .build();
    User user = new User(1L, "username", "email@ya.ru", null);

    @Test
    public void shouldAddUser() throws Exception {
        when(userService.addUser(ArgumentMatchers.any()))
                .thenReturn(user);
        when(userDtoMapper.mapToDto(ArgumentMatchers.any()))
                .thenReturn(userDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail()), String.class));
    }

    @Test
    public void shouldFailAddUserWithoutName() throws Exception {
        user = new User(1L, null, "email@ya.ru", null);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(400));
    }

    @Test
    public void shouldFailAddUserWithWrongEmail() throws Exception {
        user = new User(1L, "username", " ", null);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(400));
    }

    @Test
    public void shouldGetAll() throws Exception {
        when(userService.getAll())
                .thenReturn(List.of(user, user));
        when(userDtoMapper.mapToDto(ArgumentMatchers.any()))
                .thenReturn(userDto);

        mvc.perform(get("/users"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(1L), Long.class))
                .andExpect(jsonPath("$.[0].name", is("username"), String.class))
                .andExpect(jsonPath("$.[0].email", is("email@ya.ru"), String.class));
    }

    @Test
    public void shouldGetById() throws Exception {
        when(userService.getById(anyInt()))
                .thenReturn(user);
        when(userDtoMapper.mapToDto(ArgumentMatchers.any()))
                .thenReturn(userDto);

        mvc.perform(get("/users/1"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("username"), String.class))
                .andExpect(jsonPath("$.email", is("email@ya.ru"), String.class));
    }

    @Test
    public void shouldUpdateById() throws Exception {
        when(userService.updateById(anyInt(), any()))
                .thenReturn(user);
        when(userDtoMapper.mapToDto(ArgumentMatchers.any()))
                .thenReturn(userDto);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("username"), String.class))
                .andExpect(jsonPath("$.email", is("email@ya.ru"), String.class));
    }

    @Test
    public void shouldDeleteById() throws Exception {
        mvc.perform(delete("/users/1"))
                .andExpect(status().is(200));
    }
}