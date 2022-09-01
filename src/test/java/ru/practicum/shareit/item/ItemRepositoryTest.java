package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.util.CustomPageable;

import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    public void verifySearchItems() {
        User user = new User(null, "username", "email@ya.ru", null);
        assertThat(user.getId(), nullValue());
        userRepository.save(user);
        assertThat(user.getId(), notNullValue());
        assertThat(user.getId(), equalTo(1L));

        Item krest = new Item(null, "отвертка", "крестовая", user, true, null, null);

        assertThat(krest.getId(), nullValue());
        itemRepository.save(krest);
        assertThat(krest.getId(), notNullValue());
        assertThat(krest.getId(), equalTo(1L));

        Item shlic = new Item(null, "отвертка", "шлицевая", user, true, null, null);

        assertThat(shlic.getId(), nullValue());
        itemRepository.save(shlic);
        assertThat(shlic.getId(), notNullValue());
        assertThat(shlic.getId(), equalTo(2L));

        Pageable page = CustomPageable.of(0, 3, Sort.sort(Item.class).by(Item::getId).ascending());
        assertThat(itemRepository.searchItems("ss", page).getContent(), hasSize(0));

        List<Item> krestResult = itemRepository.searchItems("крест", page).getContent();
        assertThat(krestResult, hasSize(1));
        assertThat(krestResult, hasItem(krest));

        List<Item> shlicResult = itemRepository.searchItems("шлиц", page).getContent();
        assertThat(shlicResult, hasSize(1));
        assertThat(shlicResult, hasItem(shlic));

        List<Item> otvertResult = itemRepository.searchItems("отверт", page).getContent();
        assertThat(otvertResult, hasSize(2));
        assertThat(otvertResult, hasItem(shlic));
        assertThat(otvertResult, hasItem(krest));

        TypedQuery<Item> query = em.getEntityManager()
                .createQuery("SELECT it FROM Item it " +
                        "WHERE it.available = true " +
                        "AND (LOWER(it.name) LIKE LOWER(CONCAT('%', :text, '%') ) " +
                        "OR LOWER(it.description) LIKE LOWER(concat('%', :text, '%') ))", Item.class);
        List<Item> otvertResultFromQuery = query.setParameter("text", "отверт").getResultList();
        assertThat(otvertResult, equalTo(otvertResultFromQuery));
    }
}
