package ru.practicum.shareit.requests;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class ItemRequestTest {

    private ItemRequest itemRequest = new ItemRequest(1L, null, null, null);
    private ItemRequest equalItemRequest = new ItemRequest(1L, null, null, null);
    private ItemRequest otherItemRequest = new ItemRequest(7L, null, null, null);


    @Test
    public void checkEquals() {
        assertThat(itemRequest, equalTo(equalItemRequest));
        assertThat(itemRequest, not(otherItemRequest));
    }

    @Test
    public void checkHashCode() {
        assertThat(itemRequest.hashCode(), equalTo(equalItemRequest.hashCode()));
        assertThat(itemRequest.hashCode(), not(otherItemRequest.hashCode()));
    }
}
