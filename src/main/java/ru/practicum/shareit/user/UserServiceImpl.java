package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

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
    public User getById(long id) {
        return userRepository.getById(id);
    }

    @Override
    public User updateUser(long userId, UserDto updatesOfUser) {
        User userFromRepo = userRepository.getById(userId);
        if (userFromRepo == null) throw new NoSuchElementException("No such userId in repo");
        if (updatesOfUser.getEmail() != null) {
            userFromRepo.setEmail(updatesOfUser.getEmail());
        }
        if (updatesOfUser.getName() != null) {
            userFromRepo.setName(updatesOfUser.getName());
        }
        return userRepository.updateUser(userFromRepo);
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}
