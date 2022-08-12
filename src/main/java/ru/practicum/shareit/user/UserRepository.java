package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.Collection;

public interface UserRepository extends JpaRepository<User, Long> {
}
