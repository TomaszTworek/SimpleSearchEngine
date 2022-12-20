package com.findwise.services;

import com.findwise.exceptions.IncorrectInputException;
import com.findwise.model.IndexEntry;
import com.findwise.model.IndexEntryImpl;
import com.findwise.exceptions.NoSuchInputException;

import java.util.*;
import java.util.regex.Pattern;

public class SearchEngineImpl implements SearchEngine {

    private Map<String, List<IndexEntry>> invertedIndex = new HashMap<>();

    private Map<String, Double> documentsTfsCache = new HashMap<>();

    private int totalDocuments = 0;
    private int totalWordsInDocument = 0;


    @Override
    public void indexDocument(String id, String content) {
        totalDocuments++;

        final String[] words = splitContentIntoWords(content);

        final Map<String, Integer> countedWords = countWords(words);

        for (Map.Entry<String, Integer> entry : countedWords.entrySet()) {
            final String term = entry.getKey();
            final double occurrences = countedWords.get(term);
            double tf = calculateTf(occurrences);
            documentsTfsCache.put(id + term, tf);

            IndexEntry indexEntry = new IndexEntryImpl(id, tf);
            if (!invertedIndex.containsKey(term)) {
                invertedIndex.put(term, new ArrayList<>());
            }

            List<IndexEntry> indexEntries = invertedIndex.get(term);
            indexEntries.add(indexEntry);

            invertedIndex.put(term, indexEntries);
        }

        recalculateScoreWithCurrentIdf();

        totalWordsInDocument = 0;
    }

    private String[] splitContentIntoWords(String content) {
        return content.split("\\W+");
    }

    private Map<String, Integer> countWords(String[] words) {
        Map<String, Integer> wordsCount = new HashMap<>();
        for (String word : words) {
            final String formattedWord = word.toLowerCase();
            if (!word.isEmpty()) {
                if (wordsCount.containsKey(word)) {
                    wordsCount.put(formattedWord, wordsCount.get(formattedWord) + 1);
                } else {
                    wordsCount.put(formattedWord, 1);
                }
                totalWordsInDocument++;
            }
        }
        return wordsCount;
    }

    private double calculateTf(double occurrencesInDoc) {
        return occurrencesInDoc / totalWordsInDocument;
    }

    private void recalculateScoreWithCurrentIdf() {
        for (Map.Entry<String, List<IndexEntry>> entry : invertedIndex.entrySet()) {
            String term = entry.getKey();
            List<IndexEntry> indexEntries = invertedIndex.get(term);

            double idf = calculateIdf(indexEntries.size());
            for (IndexEntry indexEntry : indexEntries) {
                String documentName = indexEntry.getId();
                double documentTf = documentsTfsCache.get(documentName + term);
                indexEntry.setScore(documentTf * idf);
            }

            invertedIndex.put(term, indexEntries);
        }
    }

    private double calculateIdf(double occurrencesInDocs) {
        return 1 + Math.log10(totalDocuments / occurrencesInDocs);
    }

    @Override
    public List<IndexEntry> search(String term) throws NoSuchInputException, IncorrectInputException {
        if (term.isEmpty() || notATerm(term)) {
            throw new IncorrectInputException();
        }
        if (!invertedIndex.containsKey(term)) {
            throw new NoSuchInputException();
        }
        sortByTdIdf(term);
        return invertedIndex.get(term);
    }

    private boolean notATerm(String term) {
        final Pattern pattern = Pattern.compile("\\W+");
        return pattern.matcher(term.trim()).find();
    }

    private void sortByTdIdf(String term) {
        invertedIndex.get(term).sort(Comparator.comparingDouble(IndexEntry::getScore).reversed());
    }
}


