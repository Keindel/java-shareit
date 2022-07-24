package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private long userId = 0;
    private final Map<Long, User> users = new HashMap<>();

    private void validateEmail(User user) {
        if (users.values().stream().filter(otherUser -> otherUser.getId() != user.getId())
                .map(User::getEmail).anyMatch(repoUser -> repoUser.equalsIgnoreCase(user.getEmail()))) {
            throw new IllegalArgumentException("email is already registered");
        }
    }

    @Override
    public User addUser(User user) {
        validateEmail(user);
        userId++;
        user.setId(userId);
        users.put(userId, user);
        return user;
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User getById(long id) throws UserNotFoundException {
        User user = users.get(id);
        if (user == null) throw new UserNotFoundException("No such userId in repo");
        return user;
    }

    @Override
    public User updateById(User user) {
        validateEmail(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteById(long id) {
        users.remove(id);
    }
}
