package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class UserTest {

    private User item = new User(1L, null, null, null);
    private User equalUser = new User(1L, null, null, null);
    private User otherUser = new User(7L, null, null, null);


    @Test
    public void checkEquals() {
        assertThat(item, equalTo(equalUser));
        assertThat(item, not(otherUser));
    }

    @Test
    public void checkHashCode() {
        assertThat(item.hashCode(), equalTo(equalUser.hashCode()));
        assertThat(item.hashCode(), not(otherUser.hashCode()));
    }
}
