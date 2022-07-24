package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addUser(@Valid @RequestBody User user) {
        return UserDtoMapper.mapToDto(userService.addUser(user));
    }

    @GetMapping
    public Collection<UserDto> getAll() {
        return userService.getAll().stream().map(UserDtoMapper::mapToDto).collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable long userId) throws UserNotFoundException {
        return UserDtoMapper.mapToDto(userService.getById(userId));
    }

    @PatchMapping("/{userId}")
    public UserDto updateById(@PathVariable long userId, @RequestBody UserDto userDto) throws UserNotFoundException {
        return UserDtoMapper.mapToDto(userService.updateById(userId, userDto));
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable long userId) {
        userService.deleteById(userId);
    }
}
