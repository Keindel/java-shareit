package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

//@Repository
//@RequiredArgsConstructor
//public class ItemRepositoryImpl implements ItemRepository {

//    @Override
//    public Collection<Item> searchItems(String text) {
//        if (text.isBlank()) {
//            return List.of();
//        }
//        return usersItems.values().stream()
//                .flatMap(Collection::stream)
//                .filter(Item::getAvailable)
//                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
//                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
//                .collect(Collectors.toList());
//    }
//}