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
    public UserDto addUser(@RequestBody @Valid User user) {
        return UserDtoMapper.mapToDto(userService.addUser(user));
    }

    @GetMapping
    public UserDto getById(@PathVariable long userId) {
        return UserDtoMapper.mapToDto(userService.getById(userId));
    }

    @PatchMapping
    public UserDto updateUser(@RequestBody @Valid User user) {
        return UserDtoMapper.mapToDto(userService.updateUser(user));
    }

    @DeleteMapping
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }
}
