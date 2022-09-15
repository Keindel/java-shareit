package ru.practicum.shareit.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.user.User;

import javax.validation.ValidationException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class CustomPageableTest {

    private final CustomPageable customPageable = new CustomPageable(0, 3, Sort.unsorted());

    @Test
    public void checkOf() {
        Assertions.assertThrows(ValidationException.class, () ->
                CustomPageable.of(-1, 3));
        Assertions.assertThrows(ValidationException.class, () ->
                CustomPageable.of(3, -1));

        assertThat(CustomPageable.of(0, 3),
                equalTo(customPageable));
        assertThat(CustomPageable.of(null, null),
                equalTo(new CustomPageable(0, 20, Sort.unsorted())));

        assertThat(CustomPageable.of(null, null, Sort.sort(User.class)),
                equalTo(new CustomPageable(0, 20, Sort.sort(User.class))));
        assertThat(CustomPageable.of(0, 3, Sort.sort(User.class)),
                equalTo(new CustomPageable(0, 3, Sort.sort(User.class))));
    }

    @Test
    public void checkWithPage() {
        assertThat(customPageable.withPage(4),
                equalTo(new CustomPageable(12, 3, Sort.unsorted())));
    }

    @Test
    public void checkNext() {
        assertThat(customPageable.next(),
                equalTo(new CustomPageable(3, 3, Sort.unsorted())));
    }

    @Test
    public void checkPreviousOrFirst() {
        assertThat(customPageable.previousOrFirst(),
                equalTo(customPageable));
    }

    @Test
    public void checkFirst() {
        assertThat(customPageable.first(),
                equalTo(customPageable));
    }

    @Test
    public void checkGetPageNumber() {
        assertThat(customPageable.getPageNumber(),
                equalTo(0));
    }

    @Test
    public void checkHasPrevious() {
        assertThat(customPageable.hasPrevious(),
                equalTo(false));
    }
}
