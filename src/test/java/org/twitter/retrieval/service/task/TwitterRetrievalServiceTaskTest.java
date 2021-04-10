package org.twitter.retrieval.service.task;

import org.twitter.retrieval.Application;
import org.twitter.retrieval.cache.TweetsPerAuthorMap;
import org.twitter.retrieval.oauth.twitter.TwitterAuthenticationBasicClient;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
class TwitterRetrievalServiceTaskTest {

    @Test
    void givenWhenClientOk_thenRun() throws InterruptedException {
        String retrieveNumber = "6";

        TweetsPerAuthorMap tweetsPerAuthorMap = Mockito.mock(TweetsPerAuthorMap.class);
        Mockito.doNothing().when(tweetsPerAuthorMap).put(Mockito.any(), Mockito.any());

        TwitterAuthenticationBasicClient twitterAuthenticationBasicClient = Mockito.mock(TwitterAuthenticationBasicClient.class);
        Mockito.doReturn("{\"created_at\":\"Tue Feb 25 21:55:06 +0000 2020\",\"id\":1232423803898224642,\"id_str\":\"1232423803898224642\",\"text\":\"Justin Bieber\",\"display_text_range\":[0,92],\"source\":\"\",\"truncated\":false,\"in_reply_to_status_id\":1232423564936142848,\"in_reply_to_status_id_str\":\"1232423564936142848\",\"in_reply_to_user_id\":1197522168487272448,\"in_reply_to_user_id_str\":\"1197522168487272448\",\"in_reply_to_screen_name\":\"veranoFloral\",\"user\":{\"id\":1197522168487272448,\"id_str\":\"1197522168487272448\",\"name\":\"Amelie.\",\"screen_name\":\"veranoFloral\",\"location\":\"DOVS. \\u2661\",\"url\":null,\"description\":\"Francesa con ra\\u00edces latinas. \\u23af\\u23af Una mujer que est\\u00e1 enamorada de su m\\u00e1s adorada profesi\\u00f3n. Completamente enamorada de las pel\\u00edculas de Disney.\",\"translator_type\":\"none\",\"protected\":false,\"verified\":false,\"followers_count\":1785,\"friends_count\":1964,\"listed_count\":0,\"favourites_count\":9231,\"statuses_count\":8,\"created_at\":\"Thu Nov 21 14:28:37 +0000 2019\",\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":false,\"lang\":null,\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"000000\",\"profile_background_image_url\":\"\",\"profile_background_image_url_https\":\"\",\"profile_background_tile\":false,\"profile_link_color\":\"999999\",\"profile_sidebar_border_color\":\"000000\",\"profile_sidebar_fill_color\":\"000000\",\"profile_text_color\":\"000000\",\"profile_use_background_image\":false,\"profile_image_url\":\"\",\"profile_image_url_https\":\"\",\"profile_banner_url\":\"\",\"default_profile\":false,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"quote_count\":0,\"reply_count\":0,\"retweet_count\":0,\"favorite_count\":0,\"entities\":{\"hashtags\":[],\"urls\":[],\"user_mentions\":[],\"symbols\":[],\"media\":[{\"id\":1232423794427547648,\"id_str\":\"1232423794427547648\",\"indices\":[93,116],\"media_url\":\"\",\"media_url_https\":\"\",\"url\":\"\",\"display_url\":\"\",\"expanded_url\":\"h\",\"type\":\"photo\",\"sizes\":{\"thumb\":{\"w\":150,\"h\":150,\"resize\":\"crop\"},\"medium\":{\"w\":600,\"h\":751,\"resize\":\"fit\"},\"small\":{\"w\":543,\"h\":680,\"resize\":\"fit\"},\"large\":{\"w\":600,\"h\":751,\"resize\":\"fit\"}}}]},\"extended_entities\":{\"media\":[{\"id\":1232423794427547648,\"id_str\":\"1232423794427547648\",\"indices\":[93,116],\"media_url\":\"\",\"media_url_https\":\"\",\"url\":\"\",\"display_url\":\"\",\"type\":\"photo\",\"sizes\":{\"thumb\":{\"w\":150,\"h\":150,\"resize\":\"crop\"},\"medium\":{\"w\":600,\"h\":751,\"resize\":\"fit\"},\"small\":{\"w\":543,\"h\":680,\"resize\":\"fit\"},\"large\":{\"w\":600,\"h\":751,\"resize\":\"fit\"}}}]},\"favorited\":false,\"retweeted\":false,\"possibly_sensitive\":false,\"filter_level\":\"low\",\"lang\":\"es\",\"timestamp_ms\":\"1582667706833\"}").when(twitterAuthenticationBasicClient).poll();

        TwitterRetrievalServiceTask twitterRetrievalServiceTask;
        twitterRetrievalServiceTask = Mockito.spy(new TwitterRetrievalServiceTask(tweetsPerAuthorMap,
                twitterAuthenticationBasicClient,
                "1",
                "2",
                "3",
                "4",
                "5",
                retrieveNumber,
                "7"));
        twitterRetrievalServiceTask.run();

        verify(twitterAuthenticationBasicClient, times(Integer.parseInt(retrieveNumber))).isDone();
        verify(twitterAuthenticationBasicClient, times(Integer.parseInt(retrieveNumber))).poll();
        verify(tweetsPerAuthorMap, times(Integer.parseInt(retrieveNumber))).put(Mockito.any(), (Mockito.any()));
    }

    @Test
    void givenWhenClientIsDone_thenException() throws InterruptedException {
        String retrieveNumber = "6";

        TweetsPerAuthorMap tweetsPerAuthorMap = Mockito.mock(TweetsPerAuthorMap.class);
        Mockito.doNothing().when(tweetsPerAuthorMap).put(Mockito.any(), Mockito.any());

        TwitterAuthenticationBasicClient twitterAuthenticationBasicClient = Mockito.mock(TwitterAuthenticationBasicClient.class);
        Mockito.doReturn(true).when(twitterAuthenticationBasicClient).isDone();

        TwitterRetrievalServiceTask twitterRetrievalServiceTask;
        twitterRetrievalServiceTask = Mockito.spy(new TwitterRetrievalServiceTask(tweetsPerAuthorMap,
                twitterAuthenticationBasicClient,
                "1",
                "2",
                "3",
                "4",
                "5",
                retrieveNumber,
                "7"));

        twitterRetrievalServiceTask.run();

        verify(twitterAuthenticationBasicClient, times(0)).poll();
        verify(tweetsPerAuthorMap, times(0)).put(Mockito.any(), (Mockito.any()));
    }
}