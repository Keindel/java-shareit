package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @MockBean
    private BookingDtoMapper bookingDtoMapper;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    LocalDateTime nowPlus10s = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusSeconds(10);
    LocalDateTime nowPlusMin = LocalDateTime.now().plusMinutes(1).truncatedTo(ChronoUnit.SECONDS);
    BookingInputDto bookingInputDto = BookingInputDto.builder()
            .id(1L)
            .start(nowPlus10s)
            .end(nowPlusMin)
            .itemId(10L)
            .build();
    BookingOutputDto bookingOutputDto = BookingOutputDto.builder()
            .id(1L)
            .start(nowPlus10s)
            .end(nowPlusMin)
            .status(Status.APPROVED)
            .build();

    @Test
    public void shouldMakeBooking() throws Exception {
        when(bookingService.makeBooking(anyLong(), any()))
                .thenReturn(new Booking());
        when(bookingDtoMapper.mapToOutputDto(any()))
                .thenReturn(bookingOutputDto);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingInputDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingInputDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingInputDto.getStart().toString()), String.class))
                .andExpect(jsonPath("$.end", is(bookingInputDto.getEnd().toString()), String.class));
    }

    @Test
    public void shouldUpdateStatus() throws Exception {
        when(bookingService.updateStatus(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(new Booking());
        when(bookingDtoMapper.mapToOutputDto(any()))
                .thenReturn(bookingOutputDto);

        mvc.perform(patch("/bookings/7")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingInputDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingInputDto.getStart().toString()), String.class))
                .andExpect(jsonPath("$.end", is(bookingInputDto.getEnd().toString()), String.class))
                .andExpect(jsonPath("$.status", is(bookingOutputDto.getStatus().toString()), String.class));
    }

    @Test
    public void shouldGetById() throws Exception {
        when(bookingService.getById(anyLong(), anyLong()))
                .thenReturn(new Booking());
        when(bookingDtoMapper.mapToOutputDto(any()))
                .thenReturn(bookingOutputDto);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingInputDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingInputDto.getStart().toString()), String.class))
                .andExpect(jsonPath("$.end", is(bookingInputDto.getEnd().toString()), String.class))
                .andExpect(jsonPath("$.status", is(bookingOutputDto.getStatus().toString()), String.class));
    }

    @Test
    public void shouldGetAllByBookerId() throws Exception {
        when(bookingService.getAllByBookerId(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(new Booking()));
        when(bookingDtoMapper.mapToDtoCollection(any()))
                .thenReturn(List.of(bookingOutputDto));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(bookingInputDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].start", is(bookingInputDto.getStart().toString()), String.class))
                .andExpect(jsonPath("$.[0].end", is(bookingInputDto.getEnd().toString()), String.class))
                .andExpect(jsonPath("$.[0].status", is(bookingOutputDto.getStatus().toString()), String.class));
    }

    @Test
    public void shouldGetAllByOwnerId() throws Exception {
        when(bookingService.getAllByOwnerId(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(new Booking()));
        when(bookingDtoMapper.mapToDtoCollection(any()))
                .thenReturn(List.of(bookingOutputDto));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(bookingInputDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].start", is(bookingInputDto.getStart().toString()), String.class))
                .andExpect(jsonPath("$.[0].end", is(bookingInputDto.getEnd().toString()), String.class))
                .andExpect(jsonPath("$.[0].status", is(bookingOutputDto.getStatus().toString()), String.class));
    }
}
