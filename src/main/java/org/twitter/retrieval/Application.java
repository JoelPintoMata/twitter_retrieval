package org.twitter.retrieval;

import org.twitter.retrieval.service.TwitterRetrievalService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * TwitterRetrieval main class
 */
@SpringBootApplication
public class Application {

    /**
     * TwitterRetrieval main method
     * @param args
     */
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Application.class, args);

        TwitterRetrievalService twitterRetrievalService = applicationContext.getBean(TwitterRetrievalService.class);
        twitterRetrievalService.execute(false);
    }
}