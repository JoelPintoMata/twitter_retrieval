package org.twitter.retrieval.writer;

public interface TweetWriter {

    /**
     * Writes data to a file
     *
     * @param data the data to write
     */
    void write(String data);
}