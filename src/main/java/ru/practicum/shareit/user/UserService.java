package ru.practicum.shareit.user;

import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    User addUser(User user);

    Collection<User> getAll();

    User getById(long id) throws UserNotFoundException;

    User updateById(long userId, UserDto updatesOfUser) throws UserNotFoundException;

    void deleteById(long id);
}
