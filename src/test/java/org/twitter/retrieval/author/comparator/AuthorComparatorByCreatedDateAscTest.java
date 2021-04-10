package org.twitter.retrieval.author.comparator;

import org.twitter.retrieval.Application;
import org.twitter.retrieval.author.model.Author;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
class AuthorComparatorByCreatedDateAscTest {

    @Autowired
    private AuthorComparatorByCreatedDateAsc authorComparatorByCreatedDateAsc;

    @Test
    void givenTwoDates_whenSecondLaterThenFirst_return() throws InterruptedException {
        Author two = new Author();
        two.setCreated_at(LocalDateTime.now().minusDays(1));
        Author one = new Author();
        one.setCreated_at(LocalDateTime.now());
        assertEquals(-1, authorComparatorByCreatedDateAsc.compare(two, one));
    }

    @Test
    void givenTwoDates_whenFirstLaterThenSecond_return() {
        Author one = new Author();
        one.setCreated_at(LocalDateTime.now().minusDays(1));
        Author two = new Author();
        two.setCreated_at(LocalDateTime.now());
        assertEquals(1, authorComparatorByCreatedDateAsc.compare(two, one));
    }

    @Test
    void givenTwoDates_whenTheSame_returnZero() {
        Author one = new Author();
        one.setCreated_at(LocalDateTime.now());
        Author two = new Author();
        two.setCreated_at(one.getCreated_at());
        assertEquals(0, authorComparatorByCreatedDateAsc.compare(two, one));
    }
}