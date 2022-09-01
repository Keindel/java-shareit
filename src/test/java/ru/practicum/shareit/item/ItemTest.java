package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class ItemTest {



    private Item item = new Item(1L, null, null, new User(), null, null, null);
    private Item equalItem = new Item(1L, null, null, new User(), null, null, null);
    private Item otherItem = new Item(7L, null, null, new User(), null, null, null);


    @Test
    public void chechEquals() {
        assertThat(item, equalTo(equalItem));
        assertThat(item, not(otherItem));
    }

    @Test
    public void checkHashCode() {
        assertThat(item.hashCode(), equalTo(equalItem.hashCode()));
        assertThat(item.hashCode(), not(otherItem.hashCode()));
    }
}
