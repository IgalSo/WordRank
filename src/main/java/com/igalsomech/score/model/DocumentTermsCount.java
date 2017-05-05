package com.igalsomech.score.model;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represent a mapping between a term the number of times the term appears in a document.
 */
public class DocumentTermsCount {

    private Map<String, AtomicInteger> termCountMap = new ConcurrentHashMap<>();

    public DocumentTermsCount() {
    }

    public DocumentTermsCount(final DocumentTermsCount documentTermsCount) {
        this.termCountMap = new ConcurrentHashMap<>(documentTermsCount.termCountMap);
    }

    public Set<String> getTerms() {
        return Collections.unmodifiableSet(termCountMap.keySet());
    }

    public int getTermCount(final String term) {
        return termCountMap.getOrDefault(term, new AtomicInteger(0)).get();
    }

    public void incrementTermCount(final String term) {
        AtomicInteger termCount = termCountMap.computeIfAbsent(term, t -> new AtomicInteger(0));
        termCount.incrementAndGet();
    }
}
