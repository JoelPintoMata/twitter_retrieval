package org.twitter.retrieval.service;

import org.springframework.stereotype.Service;

/**
 * Template for a retrieval service
 */
@Service
public interface TwitterRetrievalService {

    /**
     * Executes a retrieval process
     *
     * @param once run this procedure once, if true, forever if otherwise
     */
    void execute(boolean once);
}