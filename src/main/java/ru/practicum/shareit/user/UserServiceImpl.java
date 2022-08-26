package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public User getById(long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User updateById(long userId, UserDto updatesOfUser) throws UserNotFoundException {
        User userFromRepo = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (updatesOfUser.getEmail() != null) {
            userFromRepo.setEmail(updatesOfUser.getEmail());
        }
        if (updatesOfUser.getName() != null) {
            userFromRepo.setName(updatesOfUser.getName());
        }
        return userRepository.save(userFromRepo);
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}
