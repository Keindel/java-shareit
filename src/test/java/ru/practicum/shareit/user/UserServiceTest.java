package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void shouldFailUpdateOnWrongUserId() {
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.updateById(anyLong(), UserDto.builder().id(1).build());
        });
    }

    @Test
    public void shouldUpdateUserEmail() throws UserNotFoundException {
        long id = 3L;
        User user = new User(id, "oldname", "oldemail", null);
        UserDto updatesOfUser = UserDto.builder()
                .email("newemail@ya.ru")
                .build();

        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
                        .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        User savedUser = userService.addUser(user);
        User updatedUser = userService.updateById(id, updatesOfUser);

        assertThat(savedUser, equalTo(user));
        assertThat(updatedUser, hasProperty("email", equalTo(updatedUser.getEmail())));

        Mockito.verify(userRepository, Mockito.times(2)).save(user);
        Mockito.verify(userRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void shouldUpdateUserName() throws UserNotFoundException {
        long id = 3L;
        User user = new User(id, "oldname", "oldemail", null);
        UserDto updatesOfUser = UserDto.builder()
                .name("newname@ya.ru")
                .build();

        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        User savedUser = userService.addUser(user);
        User updatedUser = userService.updateById(id, updatesOfUser);

        assertThat(savedUser, equalTo(user));
        assertThat(updatedUser, hasProperty("name", equalTo(updatedUser.getName())));

        Mockito.verify(userRepository, Mockito.times(2)).save(user);
        Mockito.verify(userRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void shouldNotChangeUserOnUpdate() throws UserNotFoundException {
        long id = 3L;
        User user = new User(id, "oldname", "oldemail", null);
        UserDto updatesOfUser = UserDto.builder()
                .name(null)
                .email(null)
                .build();

        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        User savedUser = userService.addUser(user);
        User updatedUser = userService.updateById(id, updatesOfUser);

        assertThat(savedUser, equalTo(user));
        assertThat(updatedUser, hasProperty("name", equalTo(user.getName())));
        assertThat(updatedUser, hasProperty("email", equalTo(user.getEmail())));

        Mockito.verify(userRepository, Mockito.times(2)).save(user);
        Mockito.verify(userRepository, Mockito.times(1)).findById(id);
    }
}
