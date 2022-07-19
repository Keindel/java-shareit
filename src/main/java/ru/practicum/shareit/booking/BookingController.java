package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    //search and book item

    //
}


/*
2.	находить нужную вещь и брать её в аренду на какое-то время
* 3.	не только позволять бронировать вещь на определённые даты, но и закрывать к ней доступ на время бронирования от других желающих

11.	Чтобы воспользоваться нужной вещью, её требуется забронировать
12.	Бронируется вещь всегда на определённые даты
13.	Владелец вещи обязательно должен подтвердить бронирование
14.	После того как вещь возвращена, у пользователя, который её арендовал, должна быть возможность оставить отзыв
15.	В отзыве можно поблагодарить владельца вещи и подтвердить, что задача выполнена

* */