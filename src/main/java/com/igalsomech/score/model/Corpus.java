package com.igalsomech.score.model;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a "corpus" set storing documentsIds.
 */
public class Corpus {

    private Set<String> documentIds = ConcurrentHashMap.newKeySet();

    public void addDocument(final String documentId) {
        documentIds.add(documentId);
    }

    public int getDocumentCount() {
        return documentIds.size();
    }
}
