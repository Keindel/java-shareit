package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository{
    private static long userId = 0;
    private final Map<Long, User> users = new HashMap<>();

    private void validateUser(User user) {
        if (users.values().stream().map(User::getEmail).anyMatch(x->x.equalsIgnoreCase(user.getEmail()))) {
            throw new IllegalArgumentException("email is already registered");
        }
    }

    @Override
    public User addUser(User user) {
        validateUser(user);
        userId++;
        user.setId(userId);
        users.put(userId, user);
        return user;
    }

    @Override
    public User getById(long id) {
        return users.get(id);
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteById(long id) {
        users.remove(id);
    }
}
