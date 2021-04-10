package org.twitter.retrieval.cache;

import org.twitter.retrieval.author.comparator.AuthorComparator;
import org.twitter.retrieval.author.model.Author;
import org.twitter.retrieval.tweet.comparator.TweetComparator;
import org.twitter.retrieval.tweet.model.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Caching system to keep all author tweets
 */
@Component
public class TweetsPerAuthorMap {

    private AuthorComparator authorComparator;
    private TweetComparator tweetComparator;

    private Map<Author, List<Tweet>> map;

    @Autowired
    public TweetsPerAuthorMap(AuthorComparator authorComparator,
                              TweetComparator tweetComparator) {
        this.authorComparator = authorComparator;
        this.tweetComparator = tweetComparator;

        this.map = new HashMap<>();
    }

    /**
     * Associates the specified value with the specified key in this map
     *
     * @param author the value
     * @param tweet  the key
     */
    public void put(Author author, Tweet tweet) {
        if (this.map.containsKey(author)) {
            map.get(author).add(tweet);
        } else {
            map.put(author, new ArrayList<>(Arrays.asList(tweet)));
        }
    }

    /**
     * Clears this cache
     */
    public void clear() {
        map.clear();
    }

    /**
     * Retrieves all authors
     * @return a ordered list of authors
     */
    public List<Author> getAuthors() {
        List<Author> authorList = new ArrayList<>(map.keySet());
        Collections.sort(authorList, authorComparator);
        return authorList;
    }

    /**
     * Retrieves all author tweets
     * @param author an Author
     * @return a ordered list of author tweets
     */
    public List<Tweet> getTweets(Author author) {
        List<Tweet> tweetList = map.get(author);
        Collections.sort(tweetList, tweetComparator);
        return tweetList;
    }

    /**
     * Retrieves all stored tweets
     *
     * @return a list of all stored tweets
     */
    public List<Tweet> getTweets() {
        List<Tweet> tweetList = new ArrayList<>(0);
        for (Author author : map.keySet()) {
            tweetList.addAll(map.get(author));
        }
        return tweetList;
    }
}