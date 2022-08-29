package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.dto.ItemWithNearestBookingsDto;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @MockBean
    private ItemDtoMapper itemDtoMapper;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    Item item = new Item(1L, "itemname", "dd", new User(), true, null, null);
    ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("itemname")
            .description("dd")
            .build();
    ItemWithNearestBookingsDto itemWithNearestBookingsDto = ItemWithNearestBookingsDto.builder()
            .id(item.getId())
            .name(item.getName())
            .description(item.getDescription())
            .build();

    @Test
    public void shouldAddItem() throws Exception {
        when(itemService.addItem(anyLong(), any()))
                .thenReturn(item);
        when(itemDtoMapper.mapToDto(any()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(item))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class));
    }

    @Test
    public void shouldGetAllItemsOfOwner() throws Exception {
        when(itemService.getAllItemsOfOwner(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemWithNearestBookingsDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(itemWithNearestBookingsDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(itemWithNearestBookingsDto.getName()), String.class))
                .andExpect(jsonPath("$.[0].description", is(itemWithNearestBookingsDto.getDescription()), String.class));
    }

    @Test
    public void shouldGetById() throws Exception {
        when(itemService.getById(anyLong(), anyLong()))
                .thenReturn(itemWithNearestBookingsDto);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemWithNearestBookingsDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemWithNearestBookingsDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemWithNearestBookingsDto.getDescription()), String.class));
    }

    @Test
    public void shouldUpdateById() throws Exception {
        when(itemService.getById(anyLong(), anyLong()))
                .thenReturn(itemWithNearestBookingsDto);
        when(itemDtoMapper.mapToDto(any()))
                .thenReturn(itemDto);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemWithNearestBookingsDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemWithNearestBookingsDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemWithNearestBookingsDto.getDescription()), String.class));
    }

    @Test
    public void shouldDeleteById() throws Exception {
        mvc.perform(delete("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSearchItems() throws Exception {
        when(itemService.searchItems(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(item));
        when(itemDtoMapper.mapToDto(ArgumentMatchers.any()))
                .thenReturn(itemDto);

        mvc.perform(get("/items/search")
                        .param("text", "sometext")
                        .param("from", "0")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.[0].description", is(itemDto.getDescription()), String.class));
    }

    @Test
    public void shouldAddComment() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .id(8L)
                .text("text")
                .authorName("John")
                .build();

        when(itemService.addComment(anyLong(), any(), anyLong()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/6/comment")
                        .param("text", "sometext")
                        .param("from", "0")
                        .param("size", "3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 4L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText()), String.class))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName()), String.class));
    }
}
