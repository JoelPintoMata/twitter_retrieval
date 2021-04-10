package org.twitter.retrieval.author.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model class for the Authors
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss Z yyyy");

    private String id;
    private LocalDateTime created_at;
    private String name;
    private String screen_name;

    @JsonSetter(value = "created_at")
    public void setCreated_atFromString(String str) {
        this.created_at = LocalDateTime.parse(str, formatter);
    }
}