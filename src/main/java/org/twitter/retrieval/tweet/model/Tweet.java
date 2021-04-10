package org.twitter.retrieval.tweet.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.twitter.retrieval.author.model.Author;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model class for the Tweets
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss Z yyyy");

    private String id;
    private LocalDateTime created_at;
    private String text;
    private Author author;

    @JsonSetter(value = "created_at")
    public void setCreated_atFromString(String str) {
        this.created_at = LocalDateTime.parse(str, formatter);
    }
}