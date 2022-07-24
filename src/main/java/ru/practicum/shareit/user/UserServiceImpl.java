package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public User addUser(User user) {
        return userRepository.addUser(user);
    }

    @Override
    public Collection<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(long id) throws UserNotFoundException {
        return userRepository.getById(id);
    }

    @Override
    public User updateById(long userId, UserDto updatesOfUser) throws UserNotFoundException {
        User userFromRepo = userRepository.getById(userId);

        User userFromRepoCopy = new User(userFromRepo.getId(), userFromRepo.getName(),
                userFromRepo.getEmail(), userFromRepo.getItemsIdsForSharing());
        if (updatesOfUser.getEmail() != null) {
            userFromRepoCopy.setEmail(updatesOfUser.getEmail());
        }
        if (updatesOfUser.getName() != null) {
            userFromRepoCopy.setName(updatesOfUser.getName());
        }
        return userRepository.updateById(userFromRepoCopy);
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}
