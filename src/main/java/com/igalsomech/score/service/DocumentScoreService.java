package com.igalsomech.score.service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import com.igalsomech.score.model.DocumentTermsCount;
import com.igalsomech.score.model.TermScore;
import com.igalsomech.score.util.TFIDFCalculatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentScoreService {

    @Autowired
    private DocumentTermsService documentTermsService;

    private final Map<String, Set<TermScore>> documentTermScoreMap = new ConcurrentHashMap<>();

    /**
     * This method fetches all mapped terms and documents and calculates the frequency
     * of each term per document.
     * The term score stored in a map where the key is the document id and the value is a set of terms and their score.
     * (Term score are sorted by the score)
     */
    public void calculateDocumentsScore() {
        Set<String> processedDocumentIds = documentTermsService.getProcessedDocumentIds();
        int totalNumberOfDocuments = processedDocumentIds.size();

        processedDocumentIds.forEach(documentId -> {
            final DocumentTermsCount documentTermsCount = documentTermsService.getDocumentTermCount(documentId);
            final Set<String> documentTerms = documentTermsCount.getTerms();
            int totalNumberOfTermsInDocument = documentTerms.size();

            // Each document id mapped to a sorted synchronized set of terms score
            final Set<TermScore> termScores = documentTermScoreMap.computeIfAbsent(documentId, id -> Collections.synchronizedSortedSet(new TreeSet<>()));
            documentTerms.forEach(term -> {

                int termCount = documentTermsCount.getTermCount(term);
                int documentsCountByTerm = documentTermsService.getDocumentsCountByTerm(term);
                double tf = TFIDFCalculatorUtil.df(termCount, totalNumberOfTermsInDocument);
                double idf = TFIDFCalculatorUtil.idf(totalNumberOfDocuments, documentsCountByTerm);
                double score = TFIDFCalculatorUtil.tfidf(tf, idf);

                // Add term score per documentId
                termScores.add(new TermScore(term, score));
            });
        });
    }

    /**
     * @return an unmodifiable set of processed document ids
     */
    public Set<String> getAllProcessedDocumentIds() {
        return documentTermsService.getProcessedDocumentIds();
    }

    /**
     * @return an unmodifiable set of {@link com.igalsomech.score.model.TermScore} by document id
     */
    public Set<TermScore> getDocumentScores(final String documentId) {
        if (documentTermScoreMap.get(documentId) == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(documentTermScoreMap.get(documentId));
    }
}
