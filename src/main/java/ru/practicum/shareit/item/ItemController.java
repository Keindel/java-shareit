package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.model.Item;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    //search item

    //tell items for sharing

    //add item per request

}

/*
1.	возможность рассказывать, какими вещами они готовы поделиться
* ??????? 2.	находить нужную вещь и брать её в аренду на какое-то время
5.	По запросу можно будет добавлять новые вещи для шеринга
6.	Пользователь, который добавляет в приложение новую вещь, будет считаться ее владельцем
7.	При добавлении вещи должна быть возможность указать её краткое название и добавить небольшое описание
8.	Также у вещи обязательно должен быть статус — доступна ли она для аренды
9.	Статус должен проставлять владелец
10.	Для поиска вещей должен быть организован поиск
* */