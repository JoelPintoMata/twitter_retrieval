package org.twitter.retrieval.author.comparator;

import org.twitter.retrieval.author.model.Author;
import org.springframework.stereotype.Component;

/**
 * Orders {@link Author} by ascending created date
 */
@Component
public class AuthorComparatorByCreatedDateAsc implements AuthorComparator<Author> {

    @Override
    public int compare(Author o1, Author o2) {
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