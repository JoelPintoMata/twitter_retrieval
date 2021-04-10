package org.twitter.retrieval.service.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.twitter.retrieval.author.model.Author;
import org.twitter.retrieval.cache.TweetsPerAuthorMap;
import org.twitter.retrieval.oauth.twitter.TwitterAuthenticationBasicClient;
import org.twitter.retrieval.tweet.model.Tweet;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

/**
 * The Twitter retrieval service is responsible for:
 * 1) establish a connection to twitter
 * 2) listen for tweets
 * 3) cache the tweets
 */
@Component
@Scope("prototype")
public class TwitterRetrievalServiceTask extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(TwitterRetrievalServiceTask.class);

    private TweetsPerAuthorMap tweetsPerAuthorMap;

    private TwitterAuthenticationBasicClient twitterAuthenticationBasicClient;

    private Integer retrieveNumber;
    private Integer retrieveTimeout;
    private ObjectMapper mapper = new ObjectMapper();
    private JSONParser parser = new JSONParser();
    private Authentication auth;
    private StatusesFilterEndpoint endpoint;

    @Autowired
    public TwitterRetrievalServiceTask(TweetsPerAuthorMap tweetsPerAuthorMap,
                                       TwitterAuthenticationBasicClient twitterAuthenticationBasicClient,
                                       @Value("${consumer.api.key}") String consumerApiKey,
                                       @Value("${consumer.api.secret.key}") String consumerApiSecretKey,
                                       @Value("${consumer.access.token}") String consumerAccessToken,
                                       @Value("${consumer.access.token.secret}") String consumerAccessTokenSecret,
                                       @Value("${consumer.term}") String consumerTerm,
                                       @Value("${retrieve.number}") String retrieveNumber,
                                       @Value("${retrieve.timeout}") String retrieveTimeout) {
        this.mapper.setDateFormat(new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.getDefault()));

        this.tweetsPerAuthorMap = tweetsPerAuthorMap;
        this.twitterAuthenticationBasicClient = twitterAuthenticationBasicClient;

        this.retrieveNumber = Integer.parseInt(retrieveNumber);
        this.retrieveTimeout = Integer.parseInt(retrieveTimeout);

        // Define our endpoint: By default, delimited=length is set (we need this for our processor)
        // and stall warnings are on.
        endpoint = new StatusesFilterEndpoint();
        endpoint.stallWarnings(false);
        endpoint.trackTerms(Arrays.asList(consumerTerm));
        endpoint.delimited(true);

        auth = new OAuth1(consumerApiKey, consumerApiSecretKey, consumerAccessToken, consumerAccessTokenSecret);
    }

    @Override
    public void run() {
        try {
            // read 'retrieveNumber' number of tweets
            for (int msgRead = 0; msgRead < retrieveNumber; msgRead++) {
                if (twitterAuthenticationBasicClient.isDone()) {
                    throw new TwitterRetrieveServiceTaskException(String.format("Client connection closed unexpectedly: %s", twitterAuthenticationBasicClient.getExitEvent()));
                }

                String msg = twitterAuthenticationBasicClient.poll();
                if (msg == null) {
                    throw new TwitterRetrieveServiceTaskException(String.format("Did not receive a message in: %s seconds", retrieveTimeout));
                } else {
                    JSONObject jsonObject = (JSONObject) parser.parse(msg);
                    // create an author
                    Author author = parseAuthor((JSONObject) jsonObject.get("user"));
                    // create a tweet
                    Tweet tweet = parseTweet(jsonObject);
                    tweet.setAuthor(author);
                    tweetsPerAuthorMap.put(author, tweet);
                }
            }
        } catch (ParseException | IOException | TwitterRetrieveServiceTaskException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        twitterAuthenticationBasicClient.stop();
    }

    /**
     * Parses an Author from a json object
     *
     * @param jsonObject the json to parse
     * @return an author POJO
     * @throws IOException
     */
    private Author parseAuthor(JSONObject jsonObject) throws IOException {
        return mapper.readValue(jsonObject.toJSONString(), Author.class);
    }

    /**
     * Extracts an Tweet from a json object
     *
     * @param jsonObject the json to parse
     * @return a tweet POJO
     * @throws IOException
     */
    private Tweet parseTweet(JSONObject jsonObject) throws IOException {
        return mapper.readValue(jsonObject.toJSONString(), Tweet.class);
    }
}