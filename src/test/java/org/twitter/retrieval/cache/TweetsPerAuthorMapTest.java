package org.twitter.retrieval.cache;

import org.twitter.retrieval.author.comparator.AuthorComparator;
import org.twitter.retrieval.author.comparator.AuthorComparatorByCreatedDateAsc;
import org.twitter.retrieval.author.model.Author;
import org.twitter.retrieval.tweet.comparator.TweetComparator;
import org.twitter.retrieval.tweet.comparator.TweetComparatorByCreatedDateAsc;
import org.twitter.retrieval.tweet.model.Tweet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
class TweetsPerAuthorMapTest {

    private TweetsPerAuthorMap tweetsPerAuthorMap;

    @BeforeEach
    public void beforeEach() {
        AuthorComparator authorComparator = new AuthorComparatorByCreatedDateAsc();
        TweetComparator tweetComparator = new TweetComparatorByCreatedDateAsc();
        this.tweetsPerAuthorMap = new TweetsPerAuthorMap(authorComparator, tweetComparator);
    }

    @Test
    void put() {
        Author author1 = new Author();
        author1.setName("author 1");
        author1.setCreated_at(LocalDateTime.now());
        Author author2 = new Author();
        author2.setName("author 2");
        author2.setCreated_at(LocalDateTime.now().minusDays(1));

        Tweet tweet1 = new Tweet();
        tweet1.setText("tweet 1");
        tweet1.setCreated_at(LocalDateTime.now());
        Tweet tweet2 = new Tweet();
        tweet2.setText("tweet 2");
        tweet2.setCreated_at(LocalDateTime.now().minusDays(1));
        Tweet tweet3 = new Tweet();
        tweet3.setText("tweet 3");
        tweet3.setCreated_at(LocalDateTime.now());

        tweetsPerAuthorMap.put(author1, tweet1);
        tweetsPerAuthorMap.put(author1, tweet2);
        tweetsPerAuthorMap.put(author2, tweet3);

        assertEquals(2, tweetsPerAuthorMap.getAuthors().size());
        Assertions.assertEquals(author2.getName(), tweetsPerAuthorMap.getAuthors().get(0).getName());
        Assertions.assertEquals(author1.getName(), tweetsPerAuthorMap.getAuthors().get(1).getName());

        assertEquals(2, tweetsPerAuthorMap.getTweets(author1).size());
        Assertions.assertEquals(tweet2.getText(), tweetsPerAuthorMap.getTweets(author1).get(0).getText());
        Assertions.assertEquals(tweet1.getText(), tweetsPerAuthorMap.getTweets(author1).get(1).getText());
    }

    @Test
    void clear() {
        Author author1 = new Author();
        author1.setName("author 1");
        author1.setCreated_at(LocalDateTime.now());
        Author author2 = new Author();
        author2.setName("author 2");
        author2.setCreated_at(LocalDateTime.now());
        Tweet tweet1 = new Tweet();
        tweet1.setText("tweet 1");
        tweet1.setCreated_at(LocalDateTime.now());
        Tweet tweet2 = new Tweet();
        tweet2.setText("tweet 2");
        tweet2.setCreated_at(LocalDateTime.now());
        Tweet tweet3 = new Tweet();
        tweet3.setText("tweet 3");
        tweet3.setCreated_at(LocalDateTime.now());

        tweetsPerAuthorMap.put(author1, tweet1);
        tweetsPerAuthorMap.put(author1, tweet2);
        tweetsPerAuthorMap.put(author2, tweet3);

        assertEquals(3, tweetsPerAuthorMap.getTweets().size());

        tweetsPerAuthorMap.clear();
        assertEquals(0, tweetsPerAuthorMap.getTweets().size());
    }

    @Test
    void getTweets() {
        Author author1 = new Author();
        author1.setName("author 1");
        author1.setCreated_at(LocalDateTime.now());
        Author author2 = new Author();
        author2.setName("author 2");
        author2.setCreated_at(LocalDateTime.now());
        Tweet tweet1 = new Tweet();
        tweet1.setText("tweet 1");
        tweet1.setCreated_at(LocalDateTime.now());
        Tweet tweet2 = new Tweet();
        tweet2.setText("tweet 2");
        tweet2.setCreated_at(LocalDateTime.now());
        Tweet tweet3 = new Tweet();
        tweet3.setText("tweet 3");
        tweet3.setCreated_at(LocalDateTime.now());

        tweetsPerAuthorMap.put(author1, tweet1);
        tweetsPerAuthorMap.put(author1, tweet2);
        tweetsPerAuthorMap.put(author2, tweet3);

        assertEquals(3, tweetsPerAuthorMap.getTweets().size());
    }
}