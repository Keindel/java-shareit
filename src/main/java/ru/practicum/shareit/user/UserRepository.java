package ru.practicum.shareit.user;

public interface UserRepository {
    User addUser(User user);
    User getById(long id);
    User updateUser(User user);
    void deleteUser(long id);
}
