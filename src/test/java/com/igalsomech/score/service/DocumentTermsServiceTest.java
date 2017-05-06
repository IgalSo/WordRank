package com.igalsomech.score.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import com.igalsomech.score.model.DocumentTermsCount;

public class DocumentTermsServiceTest {

    private final DocumentTermsService documentTermsService = new DocumentTermsService();

    @Test
    public void loadDocumentTerms() {
        String firstTerm = "aaa";
        String secondTerm = "bbb";
        String thirdTerm = "ccc";
        String notExistingTerm = "not-existing-term";

        String firstDocId = "1";
        List<String> firstDocTerms = Stream.of(firstTerm, secondTerm, thirdTerm, firstTerm, secondTerm).collect(Collectors.toList());
        documentTermsService.loadDocumentTerms(firstDocId, firstDocTerms);

        String secondDocId = "2";
        List<String> secondDocTerms = Stream.of(thirdTerm, thirdTerm).collect(Collectors.toList());
        documentTermsService.loadDocumentTerms(secondDocId, secondDocTerms);

        // Assert document ids processed
        Set<String> processedDocumentIds = documentTermsService.getProcessedDocumentIds();
        Assert.assertNotNull(processedDocumentIds);
        Assert.assertTrue(processedDocumentIds.contains(firstDocId));
        Assert.assertTrue(processedDocumentIds.contains(secondDocId));

        // Assert document terms exists
        DocumentTermsCount documentTermsCount = documentTermsService.getDocumentTermCount(firstDocId);
        Assert.assertNotNull(documentTermsCount);
        Assert.assertEquals(firstDocTerms.stream().distinct().count(), documentTermsCount.getTerms().size());
        Assert.assertEquals(2, documentTermsCount.getTermCount(firstTerm));
        Assert.assertEquals(1, documentTermsCount.getTermCount(thirdTerm));
        Assert.assertEquals(0, documentTermsCount.getTermCount(notExistingTerm));

        documentTermsCount = documentTermsService.getDocumentTermCount(secondDocId);
        Assert.assertNotNull(documentTermsCount);
        Assert.assertEquals(secondDocTerms.stream().distinct().count(), documentTermsCount.getTerms().size());

        // Check if term inverted index mapping are valid
        int termDocumentCount = documentTermsService.getDocumentsCountByTerm(firstTerm);
        Assert.assertEquals(1, termDocumentCount);

        termDocumentCount = documentTermsService.getDocumentsCountByTerm(thirdTerm);
        Assert.assertEquals(2, termDocumentCount);

        termDocumentCount = documentTermsService.getDocumentsCountByTerm(notExistingTerm);
        Assert.assertEquals(0, termDocumentCount);
    }
}