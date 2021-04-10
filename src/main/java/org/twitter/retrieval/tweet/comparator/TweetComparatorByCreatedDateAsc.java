package org.twitter.retrieval.tweet.comparator;

import org.twitter.retrieval.tweet.model.Tweet;
import org.springframework.stereotype.Service;

/**
 * Orders {@link Tweet} by ascending created date
 */
@Service
public class TweetComparatorByCreatedDateAsc implements TweetComparator<Tweet> {

    @Override
    public int compare(Tweet o1, Tweet o2) {
        if ((o1).equals(o2)) {
            return 0;
        }
        if ((o1).getCreated_at().isBefore(o2.getCreated_at())) {
            return -1;
        }
        if ((o1).getCreated_at().isAfter(o2.getCreated_at())) {
            return 1;
        }
        return 0;
    }
}