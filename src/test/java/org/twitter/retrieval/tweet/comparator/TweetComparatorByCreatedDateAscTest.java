package org.twitter.retrieval.tweet.comparator;

import org.twitter.retrieval.Application;
import org.twitter.retrieval.tweet.model.Tweet;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
class TweetComparatorByCreatedDateAscTest {

    @Autowired
    private TweetComparatorByCreatedDateAsc tweetComparatorByCreatedDateAsc;

    @Test
    void givenTwoDates_whenSecondLaterThenFirst_return() throws InterruptedException {
        Tweet two = new Tweet();
        two.setCreated_at(LocalDateTime.now().minusDays(1));
        Tweet one = new Tweet();
        one.setCreated_at(LocalDateTime.now());
        assertEquals(-1, tweetComparatorByCreatedDateAsc.compare(two, one));
    }

    @Test
    void givenTwoDates_whenFirstLaterThenSecond_return() throws InterruptedException {
        Tweet one = new Tweet();
        one.setCreated_at(LocalDateTime.now().minusDays(1));
        Tweet two = new Tweet();
        two.setCreated_at(LocalDateTime.now());
        assertEquals(1, tweetComparatorByCreatedDateAsc.compare(two, one));
    }

    @Test
    void givenTwoDates_whenTheSame_returnZero() {
        Tweet one = new Tweet();
        one.setCreated_at(LocalDateTime.now());
        Tweet two = new Tweet();
        two.setCreated_at(one.getCreated_at());
        assertEquals(0, tweetComparatorByCreatedDateAsc.compare(two, one));
    }
}