//package org.interview;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.interview.author.cache.AuthorsTweetsCache;
//import org.interview.author.comparator.AuthorComparatorByCreatedDateAsc;
//import org.interview.author.model.Author;
//import org.interview.tweet.comparator.TweetComparatorByCreatedDateAsc;
//import org.interview.tweet.model.Tweet;
//
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
///**
// *
// * @author Joel Mata
// *
// */
//public class MainServiceTests {
//
//	private static boolean setUpIsDone = false;
//	private static AuthorsTweetsCache authorsTweetsCache;
//
//	//Supporting Test Tweet objects
//	static Tweet tweet1 = new Tweet();
//	static Tweet tweet2 = new Tweet();
//	static Tweet tweet3 = new Tweet();
//	static Tweet tweet4 = new Tweet();
//	static Tweet tweet5 = new Tweet();
//
//	//Supporting Test Author objects
//	static Author author1 = new Author();
//	static Author author2 = new Author();
//	static Author author3 = new Author();
//	static Author author4 = new Author();
//	static Author author5 = new Author();
//
//	@BeforeEach
//	public static void setUp() {
//		if (setUpIsDone) {
//	        return;
//	    }
//	    // do the setup
//		Date dateAuthor01 = new Date(2000, 01, 01, 12, 00);
//		Date dateAuthor02 = new Date(2000, 01, 02, 12, 00);
//		Date dateAuthor03 = new Date(2000, 01, 03, 12, 00);
//
//		Date dateTweet01 = new Date(2000, 01, 01, 12, 00);
//		Date dateTweet02 = new Date(2000, 01, 02, 12, 00);
//		Date dateTweet03 = new Date(2000, 01, 03, 12, 00);
//		Date dateTweet04 = new Date(2000, 01, 04, 12, 00);
//		Date dateTweet05 = new Date(2000, 01, 05, 12, 00);
//
//		author1.setId("12");
//		author1.setName("Interview Test Name 12");
//		author1.setScreen_name("Interview Test Screen Name 12");
//		author1.setCreated_at(dateAuthor03);
//
//		author2.setId("12");
//		author2.setName("Interview Test Name 12");
//		author2.setScreen_name("Interview Test Screen Name 12");
//		author2.setCreated_at(dateAuthor03);
//
//		author3.setId("11");
//		author3.setName("Interview Test Name 11");
//		author3.setScreen_name("Interview Test Screen Name 11");
//		author3.setCreated_at(dateAuthor02);
//
//		author4.setId("10");
//		author4.setName("Interview Test Name 10");
//		author4.setScreen_name("Interview Test Screen Name 10");
//		author4.setCreated_at(dateAuthor01);
//
//		author5.setId("11");
//		author5.setName("Interview Test Name 11");
//		author5.setScreen_name("Interview Test Screen Name 11");
//		author5.setCreated_at(dateAuthor02);
//
//		tweet1.setId("1");
//		tweet1.setText("Interview Test 1");
//		tweet1.setCreated_at(dateTweet01);
//
//		tweet2.setId("2");
//		tweet2.setText("Interview Test 2");
//		tweet2.setCreated_at(dateTweet02);
//
//		tweet3.setId("3");
//		tweet3.setText("Interview Test 3");
//		tweet3.setCreated_at(dateTweet03);
//
//		tweet4.setId("4");
//		tweet4.setText("Interview Test 4");
//		tweet4.setCreated_at(dateTweet04);
//
//		tweet5.setId("5");
//		tweet5.setText("Interview Test 5");
//		tweet5.setCreated_at(dateTweet05);
//
//	    setUpIsDone = true;
//	}
//
//    @BeforeEach
//    public void beforeEach(){
//        authorsTweetsCache = new AuthorsTweetsCache();
//        author1.getTweetList().clear();
//        author2.getTweetList().clear();
//        author3.getTweetList().clear();
//        author4.getTweetList().clear();
//        author5.getTweetList().clear();
//    }
//
//	@Test
//	public void testCache(){
//        author1.getTweetList().add(tweet1);
//		authorsTweetsCache.put(author1.getId(), author1);
//
//		assertEquals(1, authorsTweetsCache.size());
//	}
//
//    @Test
//    public void testTweetCacheAdd(){
//        author1.getTweetList().add(tweet1);
//        authorsTweetsCache.put(author1.getId(), author1);
//
//        assertEquals(authorsTweetsCache.values().size(), 1);
//    }
//
//    @Test
//    public void testTweetCacheUniqueAuthors(){
//        author1.getTweetList().add(tweet1);
//        authorsTweetsCache.put(author1.getId(), author1);
//
//        author2.getTweetList().add(tweet2);
//        authorsTweetsCache.put(author2.getId(), author2);
//
//        author3.getTweetList().add(tweet3);
//        authorsTweetsCache.put(author3.getId(), author3);
//
//        author4.getTweetList().add(tweet4);
//        authorsTweetsCache.put(author4.getId(), author4);
//
//        author5.getTweetList().add(tweet5);
//        authorsTweetsCache.put(author5.getId(), author5);
//
//        assertEquals(authorsTweetsCache.values().size(), 3);
//    }
//
//    @Test
//    public void testTweetCacheAuthorSorting(){
//        author1.getTweetList().add(tweet1);
//        authorsTweetsCache.put(author1.getId(), author1);
//
//        author2.getTweetList().add(tweet2);
//        authorsTweetsCache.put(author2.getId(), author2);
//
//        author3.getTweetList().add(tweet3);
//        authorsTweetsCache.put(author3.getId(), author3);
//
//        author4.getTweetList().add(tweet4);
//        authorsTweetsCache.put(author4.getId(), author4);
//
//        author5.getTweetList().add(tweet5);
//        authorsTweetsCache.put(author5.getId(), author5);
//
//        List<Author> authorList = authorsTweetsCache.values().stream().sorted(new AuthorComparatorByCreatedDateAsc()).collect(Collectors.toList());
//
//        assertEquals(authorList.get(0).getId(), author4.getId());
//        assertEquals(authorList.get(1).getId(), author3.getId());
//        assertEquals(authorList.get(2).getId(), author1.getId());
//    }
//
//    @Test
//    public void testTweetCacheAuthorCollision(){
//        author1.getTweetList().add(tweet1);
//        authorsTweetsCache.put(author1.getId(), author1);
//
//        author2.getTweetList().add(tweet2);
//        authorsTweetsCache.put(author2.getId(), author2);
//
//        author3.getTweetList().add(tweet3);
//        authorsTweetsCache.put(author3.getId(), author3);
//
//        author4.getTweetList().add(tweet4);
//        authorsTweetsCache.put(author4.getId(), author4);
//
//        author5.getTweetList().add(tweet5);
//        authorsTweetsCache.put(author5.getId(), author5);
//
//        assertEquals(2, authorsTweetsCache.get(author1.getId()).size());
//        assertEquals(2, authorsTweetsCache.get(author3.getId()).size());
//        assertEquals(1, authorsTweetsCache.get(author4.getId()).size());
//    }
//
//    @Test
//    public void testTweetCacheAuthorTweetsSorting(){
//        author1.getTweetList().add(tweet1);
//        authorsTweetsCache.put(author1.getId(), author1);
//
//        author2.getTweetList().add(tweet2);
//        authorsTweetsCache.put(author2.getId(), author2);
//
//        author3.getTweetList().add(tweet3);
//        authorsTweetsCache.put(author3.getId(), author3);
//
//        author4.getTweetList().add(tweet4);
//        authorsTweetsCache.put(author4.getId(), author4);
//
//        author5.getTweetList().add(tweet5);
//        authorsTweetsCache.put(author5.getId(), author5);
//
//        TweetComparatorByCreatedDateAsc tweetComparator = new TweetComparatorByCreatedDateAsc();
//
//        Collections.sort((author1).getTweetList(), tweetComparator);
//        assertEquals(authorsTweetsCache.get(author1.getId()).get(0), tweet1);
//        assertEquals(authorsTweetsCache.get(author1.getId()).get(1), tweet2);
//
//        Collections.sort((author3).getTweetList(), tweetComparator);
//        assertEquals(authorsTweetsCache.get(author3.getId()).get(0), tweet3);
//        assertEquals(authorsTweetsCache.get(author3.getId()).get(1), tweet5);
//    }
//
//    @Test
//    public void testTweetCacheAuthorClear(){
//        author1.getTweetList().add(tweet1);
//        authorsTweetsCache.put(author1.getId(), author1);
//
//        author2.getTweetList().add(tweet2);
//        authorsTweetsCache.put(author2.getId(), author2);
//
//        author3.getTweetList().add(tweet3);
//        authorsTweetsCache.put(author3.getId(), author3);
//
//        author4.getTweetList().add(tweet4);
//        authorsTweetsCache.put(author4.getId(), author4);
//
//        author5.getTweetList().add(tweet5);
//        authorsTweetsCache.put(author5.getId(), author5);
//
//        authorsTweetsCache.clear();
//
//        assertEquals(authorsTweetsCache.size(), 0);
//    }
//}