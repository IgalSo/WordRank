package com.igalsomech.score.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.igalsomech.score.model.DocumentTermsCount;
import org.springframework.stereotype.Service;

import com.igalsomech.score.model.Corpus;

@Service
public class DocumentTermsService {

    private final Map<String, DocumentTermsCount> documentTermCountMap = new ConcurrentHashMap<>();
    private final Map<String, Corpus> termCorpusMap = new ConcurrentHashMap<>();

    /**
     * This method does the following for each of the documentId terms:
     * 1. Store a mapping between the term and its occurrences number in the given document.
     * 2. Store an inverted mapping between the term and the given documentId.
     *
     * @param documentId
     * @param terms for the given documentId
     */
    public void loadDocumentTerms(final String documentId, final List<String> terms) {

        final DocumentTermsCount documentTermsCount = documentTermCountMap.computeIfAbsent(documentId, id -> new DocumentTermsCount());
        terms.forEach(term -> {
            // Update term count per document
            documentTermsCount.incrementTermCount(term);

            // Update inverted mapping: term -> documentIds
            final Corpus corpus = termCorpusMap.computeIfAbsent(term, t -> new Corpus());
            corpus.addDocument(documentId);
        });
    }

    /**
     * @return a clone of {@link com.igalsomech.score.model.DocumentTermsCount}
     */
    public DocumentTermsCount getDocumentTermCount(final String documentId) {
        return new DocumentTermsCount(documentTermCountMap.get(documentId));
    }

    /**
     * @return an unmodifiable set of all processed document ids
     */
    public Set<String> getProcessedDocumentIds() {
        return Collections.unmodifiableSet(documentTermCountMap.keySet());
    }

    /**
     * @return the documents count by term
     */
    public int getDocumentsCountByTerm(final String term) {
        Corpus corpus = termCorpusMap.get(term);
        if (corpus == null) {
            return 0;
        }
        return corpus.getDocumentCount();
    }
}
