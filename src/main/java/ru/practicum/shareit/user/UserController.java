package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;

import javax.validation.Valid;

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
    public UserDto getById(@PathVariable long userId) {
        return UserDtoMapper.mapToDto(userService.getById(userId));
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable long userId, @RequestBody UserDto userDto) {
        return UserDtoMapper.mapToDto(userService.updateUser(userId, userDto));
    }

    @DeleteMapping
    public void deleteById(@PathVariable long userId) {
        userService.deleteById(userId);
    }
}
