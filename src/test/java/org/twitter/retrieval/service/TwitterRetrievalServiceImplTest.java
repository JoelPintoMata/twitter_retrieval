package org.twitter.retrieval.service;

import org.twitter.retrieval.Application;
import org.twitter.retrieval.cache.TweetsPerAuthorMap;
import org.twitter.retrieval.service.task.TwitterRetrievalServiceTask;
import org.twitter.retrieval.writer.TweetWriter;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
class TwitterRetrievalServiceImplTest {

    @Test
    public void exectute() {
        TwitterRetrievalServiceTask twitterRetrievalServiceTask = Mockito.mock(TwitterRetrievalServiceTask.class);
        TweetsPerAuthorMap tweetsPerAuthorMap = Mockito.mock(TweetsPerAuthorMap.class);
        String retrieveTimeout = "10000";

        TweetWriter tweetWriter = Mockito.mock(TweetWriter.class);

        ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);
        TwitterRetrievalServiceImpl twitterRetrievalService = Mockito.spy(new TwitterRetrievalServiceImpl(tweetsPerAuthorMap, tweetWriter, retrieveTimeout));
        Mockito.doReturn(twitterRetrievalServiceTask).when(applicationContext).getBean(TwitterRetrievalServiceTask.class);

        ReflectionTestUtils.setField(twitterRetrievalService, "applicationContext", applicationContext);

        twitterRetrievalService.execute(true);

        verify(twitterRetrievalServiceTask, times(1)).start();

        verify(tweetsPerAuthorMap, times(1)).getAuthors();
        verify(tweetsPerAuthorMap, times(1)).clear();
    }
}