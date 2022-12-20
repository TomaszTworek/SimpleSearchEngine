package com.findwise.services;

import com.findwise.exceptions.IncorrectInputException;
import com.findwise.exceptions.NoSuchInputException;
import com.findwise.model.IndexEntry;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SearchEngineImplTest {

    @Test
    void shouldSearchForTermAndReturnResultsOrderedByTfIdf() throws NoSuchInputException, IncorrectInputException {
        //given
        final SearchEngine searchEngine = new SearchEngineImpl();
        final String id1 = "doc1.txt";
        final String id2 = "doc2.txt";
        final String id3 = "doc3.txt";
        final String content1 = "the brown fox jumped over the brown dog";
        final String content2 = "the lazy brown dog sat in corner";
        final String content3 = "the red fox bit the lazy dog";
        searchEngine.indexDocument(id1, content1);
        searchEngine.indexDocument(id2, content2);
        searchEngine.indexDocument(id3, content3);

        //when
        final List<IndexEntry> foxResult = searchEngine.search("fox");

        //then
        final int expectedSize = 2;
        assertEquals(expectedSize, foxResult.size());

        final IndexEntry firstElement = foxResult.get(0);
        assertEquals(firstElement.getId(), id3);
        final IndexEntry secondElement = foxResult.get(1);
        assertEquals(secondElement.getId(), id1);

        final BigDecimal resultScore1 = BigDecimal.valueOf(firstElement.getScore())
                .setScale(5, RoundingMode.HALF_UP);
        double expectedValue1 = 0.16801;
        assertEquals(BigDecimal.valueOf(expectedValue1), resultScore1);

        final BigDecimal resultScore2 = BigDecimal.valueOf(secondElement.getScore())
                .setScale(5, RoundingMode.HALF_UP);
        double expectedValue2 = 0.14701;
        assertEquals(BigDecimal.valueOf(expectedValue2), resultScore2);
    }

    @Test
    void shouldSplitWordsAndRemoveOtherSigns() throws NoSuchInputException, IncorrectInputException {
        //given
        final SearchEngine searchEngine = new SearchEngineImpl();
        final String id1 = "doc1.txt";
        final String id2 = "doc2.txt";
        final String id3 = "doc3.txt";
        final String content1 = "the, brown. fox? jumped over   the/ brown dog.";
        final String content2 = "the, lazy; brown: (dog sat)  in! corner";
        final String content3 = "the red fox bit the lazy dog";
        searchEngine.indexDocument(id1, content1);
        searchEngine.indexDocument(id2, content2);
        searchEngine.indexDocument(id3, content3);

        //when
        final List<IndexEntry> foxResult = searchEngine.search("fox");

        System.out.println(foxResult);
        //then
        final int expectedSize = 2;
        assertEquals(expectedSize, foxResult.size());

        final IndexEntry firstElement = foxResult.get(0);
        assertEquals(firstElement.getId(), id3);
        final IndexEntry secondElement = foxResult.get(1);
        assertEquals(secondElement.getId(), id1);

        final BigDecimal resultScore1 = BigDecimal.valueOf(firstElement.getScore())
                .setScale(5, RoundingMode.HALF_UP);
        double expectedValue1 = 0.16801;
        assertEquals(BigDecimal.valueOf(expectedValue1), resultScore1);

        final BigDecimal resultScore2 = BigDecimal.valueOf(secondElement.getScore())
                .setScale(5, RoundingMode.HALF_UP);
        double expectedValue2 = 0.14701;
        assertEquals(BigDecimal.valueOf(expectedValue2), resultScore2);
    }

    @Test
    void shouldThrowExceptionWhenEmptyOrNonWordIsPassed() {
        //given
        final SearchEngine searchEngine = new SearchEngineImpl();
        final String id1 = "doc1.txt";
        final String id2 = "doc2.txt";
        final String id3 = "doc3.txt";
        final String content1 = "the, brown. fox? jumped over   the/ brown dog.";
        final String content2 = "the, lazy; brown: (dog sat)  in! corner";
        final String emptyContent = "";
        searchEngine.indexDocument(id1, content1);
        searchEngine.indexDocument(id2, content2);
        searchEngine.indexDocument(id3, emptyContent);

        //when then
        assertThrows(IncorrectInputException.class, () -> searchEngine.search(""));
        assertThrows(IncorrectInputException.class, () -> searchEngine.search("[][]]$!w]"));
    }

    @Test
    void shouldThrowExceptionWhenEmptyPassedWordNotExistsInDocuments() {
        //given
        final SearchEngine searchEngine = new SearchEngineImpl();
        final String id1 = "doc1.txt";
        final String id2 = "doc2.txt";
        final String id3 = "doc3.txt";
        final String content1 = "the, brown. fox? jumped over   the/ brown dog.";
        final String content2 = "the, lazy; brown: (dog sat)  in! corner";
        final String emptyContent = "";
        searchEngine.indexDocument(id1, content1);
        searchEngine.indexDocument(id2, content2);
        searchEngine.indexDocument(id3, emptyContent);

        //when then
        assertThrows(NoSuchInputException.class, () -> searchEngine.search("test"));
    }

    @Test
    void shouldThrowExceptionWhenMoreTheOneWordIsGiven() {
        //given
        final SearchEngine searchEngine = new SearchEngineImpl();
        final String id1 = "doc1.txt";
        final String id2 = "doc2.txt";
        final String id3 = "doc3.txt";
        final String content1 = "the, brown. fox? jumped over   the/ brown dog.";
        final String content2 = "the, lazy; brown: (dog sat)  in! corner";
        final String emptyContent = "";
        searchEngine.indexDocument(id1, content1);
        searchEngine.indexDocument(id2, content2);
        searchEngine.indexDocument(id3, emptyContent);

        //when then
        assertThrows(IncorrectInputException.class, () -> searchEngine.search("the brown"));
    }
}