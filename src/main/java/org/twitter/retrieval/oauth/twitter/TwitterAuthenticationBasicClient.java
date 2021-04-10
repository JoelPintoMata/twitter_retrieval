package org.twitter.retrieval.oauth.twitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class TwitterAuthenticationBasicClient {

    // Create an appropriately sized blocking queue
    private BlockingQueue<String> queue;
    private Integer retrieveNumber;
    private Integer retrieveTimeout;
    private ObjectMapper mapper = new ObjectMapper();
    private Authentication auth;
    private StatusesFilterEndpoint endpoint;
    private BasicClient client;

    @Autowired
    public TwitterAuthenticationBasicClient(@Value("${consumer.api.key}") String consumerApiKey,
                                            @Value("${consumer.api.secret.key}") String consumerApiSecretKey,
                                            @Value("${consumer.access.token}") String consumerAccessToken,
                                            @Value("${consumer.access.token.secret}") String consumerAccessTokenSecret,
                                            @Value("${consumer.term}") String consumerTerm,
                                            @Value("${retrieve.number}") String retrieveNumber,
                                            @Value("${retrieve.timeout}") String retrieveTimeout) {
        this.mapper.setDateFormat(new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.getDefault()));

        this.retrieveNumber = Integer.parseInt(retrieveNumber);
        this.retrieveTimeout = Integer.parseInt(retrieveTimeout);

        this.queue = new LinkedBlockingQueue<>(this.retrieveNumber);

        // Define our endpoint: By default, delimited=length is set (we need this for our processor)
        // and stall warnings are on.
        endpoint = new StatusesFilterEndpoint();
        endpoint.stallWarnings(false);
        endpoint.trackTerms(Arrays.asList(consumerTerm));
        endpoint.delimited(true);

        auth = new OAuth1(consumerApiKey, consumerApiSecretKey, consumerAccessToken, consumerAccessTokenSecret);

        // Create a new BasicClient. By default gzip is enabled.
        client = new ClientBuilder()
                .hosts(Constants.USERSTREAM_HOST)
                .endpoint(endpoint)
                .authentication(auth)
                .processor(new StringDelimitedProcessor(queue))
                .build();

        this.connect();
    }

    public void connect() {
        client.connect();
    }

    public void stop() {
        client.stop();
    }

    public boolean isDone() {
        return client.isDone();
    }

    public String poll() throws InterruptedException {
        return queue.poll(retrieveTimeout, TimeUnit.SECONDS);
    }

    public String getExitEvent() {
        return client.getExitEvent().getMessage();
    }
}