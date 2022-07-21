package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.List;

public interface UserRepository {
    User addUser(User user);
    Collection<User> getAll();
    User getById(long id);
    User updateById(User user);
    void deleteById(long id);
}
