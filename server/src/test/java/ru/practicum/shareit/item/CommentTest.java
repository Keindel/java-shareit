package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.user.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class CommentTest {

    private Comment comment = new Comment(1L, null, new Item(), new User(), null);
    private Comment equalComment = new Comment(1L, null, new Item(), new User(), null);
    private Comment otherComment = new Comment(7L, null, new Item(), new User(), null);


    @Test
    public void checkEquals() {
        assertThat(comment, equalTo(equalComment));
        assertThat(comment, not(otherComment));
    }

    @Test
    public void checkHashCode() {
        assertThat(comment.hashCode(), equalTo(equalComment.hashCode()));
        assertThat(comment.hashCode(), not(otherComment.hashCode()));
    }
}
