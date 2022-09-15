package ru.practicum.shareit.booking;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingBookerIdDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@JsonTest
public class BookingDtoJsonTest {
    @Autowired
    private JacksonTester<BookingInputDto> inputDtoJacksonTester;
    @Autowired
    private JacksonTester<BookingOutputDto> outputDtoJacksonTester;
    @Autowired
    private JacksonTester<BookingBookerIdDto> bookerIdDtoJacksonTester;


    LocalDateTime nowPlus10s = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusSeconds(10);
    LocalDateTime nowPlusMin = LocalDateTime.now().plusMinutes(1).truncatedTo(ChronoUnit.SECONDS);

    @Test
    public void checkInputDto() throws Exception {
        BookingInputDto bookingInputDto = BookingInputDto.builder()
                .id(1L)
                .start(nowPlus10s)
                .end(nowPlusMin)
                .itemId(10L)
                .build();

        JsonContent<BookingInputDto> result = inputDtoJacksonTester.write(bookingInputDto);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(nowPlus10s.toString());
        Assertions.assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(nowPlusMin.toString());
        Assertions.assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(10);
    }

    @Test
    public void checkOutputDto() throws Exception {
        BookingOutputDto bookingOutputDto = BookingOutputDto.builder()
                .id(1L)
                .start(nowPlus10s)
                .end(nowPlusMin)
                .build();

        JsonContent<BookingOutputDto> result = outputDtoJacksonTester.write(bookingOutputDto);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(nowPlus10s.toString());
        Assertions.assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(nowPlusMin.toString());
    }

    @Test
    public void checkBookerIdDto() throws Exception {
        BookingBookerIdDto bookingBookerIdDto = new BookingBookerIdDto(1L, 14L);

        JsonContent<BookingBookerIdDto> result = bookerIdDtoJacksonTester.write(bookingBookerIdDto);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(14);
    }
}