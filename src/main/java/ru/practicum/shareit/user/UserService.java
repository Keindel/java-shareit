package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    User addUser(User user);
    User getById(long id);
    User updateUser(long userId, UserDto updatesOfUser);
    void deleteById(long id);
}
