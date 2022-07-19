package ru.practicum.shareit.requests;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemRequest {
    @EqualsAndHashCode.Include
    private long id;
    @NotBlank
    @Size(max = 50)
    private String itemName;
    @Size(max = 500)
    private String description;
}

/*
* 16.	Пользователь создаёт запрос, если нужная ему вещь не найдена при поиске
17.	В запросе указывается, что именно он ищет
18.	В ответ на запрос другие пользовали могут добавить нужную вещь

* */
