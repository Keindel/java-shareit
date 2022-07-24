package ru.practicum.shareit.user;

import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.Collection;

public interface UserRepository {
    User addUser(User user);

    Collection<User> getAll();

    User getById(long id) throws UserNotFoundException;

    User updateById(User user);

    void deleteById(long id);
}
