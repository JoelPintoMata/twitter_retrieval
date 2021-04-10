package org.twitter.retrieval.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * File writer
 */
@Component
public class TweetFileWriter implements TweetWriter {

    private static final Logger logger = LoggerFactory.getLogger(TweetFileWriter.class);
    private String outputFile;

    @Autowired
    public TweetFileWriter(@Value("${output.file}") String outputFile) {
        this.outputFile = outputFile;
        try {
            Files.createFile(
                    Paths.get(outputFile));
        } catch (FileAlreadyExistsException e) {
            // ignore this...
        } catch (IOException e) {
            logger.error("Could not create file: {}", outputFile, e);
        }
    }

    @Override
    public void write(String data) {
        try {
            Files.write(
                    Paths.get(outputFile),
                    data.getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("Could not write: {}", data, e);
        }
    }
}