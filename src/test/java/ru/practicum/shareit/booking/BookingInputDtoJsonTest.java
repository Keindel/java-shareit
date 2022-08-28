package ru.practicum.shareit.booking;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingInputDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@JsonTest
public class BookingInputDtoJsonTest {
    @Autowired
    private JacksonTester<BookingInputDto> json;

    @Test
    public void shouldSucceed() throws Exception {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime nowPlusMin = LocalDateTime.now().plusMinutes(1).truncatedTo(ChronoUnit.SECONDS);
        BookingInputDto bookingInputDto = BookingInputDto.builder()
                .id(1L)
                .start(now)
                .end(nowPlusMin)
                .itemId(10L)
                .build();

        JsonContent<BookingInputDto> result = json.write(bookingInputDto);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(now.toString());
        Assertions.assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(nowPlusMin.toString());
        Assertions.assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(10);
    }
}