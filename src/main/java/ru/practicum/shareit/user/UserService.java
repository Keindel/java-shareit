package ru.practicum.shareit.user;

public interface UserService {
    User addUser(User user);
    User getById(long id);
    User updateUser(long userId, User user);
    void deleteById(long id);
}
