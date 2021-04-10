package org.twitter.retrieval.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.hbc.core.endpoint.StatusesSampleEndpoint;
import org.twitter.retrieval.author.model.Author;
import org.twitter.retrieval.cache.TweetsPerAuthorMap;
import org.twitter.retrieval.service.task.TwitterRetrievalServiceTask;
import org.twitter.retrieval.tweet.model.Tweet;
import org.twitter.retrieval.writer.TweetWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * The Twitter retrieval service is responsible for:
 * 1) performs Twitter streaming API request
 * 2) listen for he tweets relevant to a given query
 * 3) cache the tweets per author
 * <p>
 * This process runs during both a time interval or maximum number of tweets
 */
@Service
public class TwitterRetrievalServiceImpl implements TwitterRetrievalService {

    private static final Logger logger = LoggerFactory.getLogger(TwitterRetrievalServiceImpl.class);

    @Autowired
    private ApplicationContext applicationContext;

    private TweetsPerAuthorMap tweetsPerAuthorMap;

    private StatusesSampleEndpoint endpoint;

    private TweetWriter tweetWriter;

    private Integer retrieveTimeout;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public TwitterRetrievalServiceImpl(TweetsPerAuthorMap tweetsPerAuthorMap,
                                       TweetWriter tweetWriter,
                                       @Value("${retrieve.timeout}") String retrieveTimeout) {
        this.tweetsPerAuthorMap = tweetsPerAuthorMap;
        this.tweetWriter = tweetWriter;

        this.retrieveTimeout = Integer.parseInt(retrieveTimeout);

        // Define our endpoint: By default, delimited=length is set (we need this for our processor)
        // and stall warnings are on.
        endpoint = new StatusesSampleEndpoint();
        endpoint.stallWarnings(false);
    }

    /**
     * Executes the twitter streaming api retrieval/process
     */
    @Override
    public void execute(boolean once) {
        try {
            while (true) {
                // TODO replace with a executors pool
                TwitterRetrievalServiceTask twitterRetrievalServiceTask = applicationContext.getBean(TwitterRetrievalServiceTask.class);
                long start = System.nanoTime();
                twitterRetrievalServiceTask.start();
                twitterRetrievalServiceTask.join(retrieveTimeout);
                long finish = System.nanoTime();

                // Print some stats
                tweetWriter.write(String.format("Read %s messages in over %s miliseconds", tweetsPerAuthorMap.getTweets().size(), finish - start));
                logger.info(String.format("Read %s messages in over %s miliseconds", tweetsPerAuthorMap.getTweets().size(), finish - start));
                tweetWriter.write("\r\n");
                List<Author> authorList = tweetsPerAuthorMap.getAuthors();
                for (Author author : authorList) {
                    List<Tweet> tweetList = tweetsPerAuthorMap.getTweets(author);
                    for (Tweet tweet : tweetList) {
                        tweetWriter.write(parseTweet(tweet));
                        logger.info(parseTweet(tweet));
                    }
                    tweetWriter.write("\r\n");
                }
                tweetsPerAuthorMap.clear();
                if (once) {
                    return;
                }
            }
        } catch (Exception e) {
            logger.error("Error while processing a tweet retrieval batch: {}", e.getMessage(), e);
        }
    }

    /**
     * Extracts a string from a tweet
     *
     * @param tweet a tweet
     * @return a string representation of a tweet
     */
    private String parseTweet(Tweet tweet) throws IOException {
        return mapper.writeValueAsString(tweet);
    }
}