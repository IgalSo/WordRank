package com.igalsomech.score.util;

/**
 * Utility used for calculating tf idf weights.
 * See more: https://en.wikipedia.org/wiki/Tf–idf
 */
public class TFIDFCalculatorUtil {

    public static double df(double termOccurrencesInDocumentCount, double totalTermsInDocumentCount) {
        return termOccurrencesInDocumentCount / totalTermsInDocumentCount;
    }

    public static double idf(double totalDocumentsCount, double termOccurrencesInDocumentsCount) {
        // In case term not exists in the corpus than we adjust the denominator
        if(termOccurrencesInDocumentsCount == 0) {
            termOccurrencesInDocumentsCount++;
        }
        return Math.log(totalDocumentsCount /  termOccurrencesInDocumentsCount);
    }

    public static double tfidf(double tf, double idf) {
        return tf * idf;
    }
}
