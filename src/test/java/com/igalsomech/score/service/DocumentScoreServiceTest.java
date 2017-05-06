package com.igalsomech.score.service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.igalsomech.score.AppConfig;
import com.igalsomech.score.model.DocumentTermsCount;
import com.igalsomech.score.model.TermScore;

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@TestPropertySource("classpath:application.properties")
public class DocumentScoreServiceTest {

    @Mock
    private DocumentTermsService documentTermsService;

    @InjectMocks
    private DocumentScoreService documentScoreService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void calculateDocumentScoreTest() {
        String firstDocument = "1";
        String secondDocument = "2";
        String firstTerm = "term1";
        String secondTerm = "term2";

        // Validate data not mapped
        documentScoreService.calculateDocumentsScore();
        Set<TermScore> documentScores = documentScoreService.getDocumentScores(firstDocument);
        Assert.assertNotNull(documentScores);
        Assert.assertEquals(0, documentScores.size());

        DocumentTermsCount firstDocumentTermsCountMock = new DocumentTermsCount();
        firstDocumentTermsCountMock.incrementTermCount(firstTerm);
        firstDocumentTermsCountMock.incrementTermCount(firstTerm);
        firstDocumentTermsCountMock.incrementTermCount(secondTerm);

        DocumentTermsCount secondDocumentTermsCountMock = new DocumentTermsCount();
        secondDocumentTermsCountMock.incrementTermCount(firstTerm);

        // Mocking 2 documents with terms
        Mockito.when(documentTermsService.getProcessedDocumentIds()).thenReturn(Stream.of(firstDocument, secondDocument).collect(Collectors.toSet()));
        Mockito.when(documentTermsService.getDocumentTermCount(firstDocument)).thenReturn(firstDocumentTermsCountMock);
        Mockito.when(documentTermsService.getDocumentTermCount(secondDocument)).thenReturn(secondDocumentTermsCountMock);

        // Assert documents and terms score
        documentScoreService.calculateDocumentsScore();
        documentScores = documentScoreService.getDocumentScores(firstDocument);
        Assert.assertNotNull(documentScores);
        Assert.assertEquals(firstDocumentTermsCountMock.getTerms().size(), documentScores.size());

        documentScores = documentScoreService.getDocumentScores(secondDocument);
        Assert.assertNotNull(documentScores);
        Assert.assertEquals(secondDocumentTermsCountMock.getTerms().size(), documentScores.size());
    }
}