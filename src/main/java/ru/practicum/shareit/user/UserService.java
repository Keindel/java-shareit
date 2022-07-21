package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    User addUser(User user);
    Collection<User> getAll();
    User getById(long id);
    User updateById(long userId, UserDto updatesOfUser);
    void deleteById(long id);
}
