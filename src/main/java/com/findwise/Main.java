package com.findwise;

import com.findwise.infrastructure.DocumentsReader;
import com.findwise.infrastructure.DocumentsSearcher;
import com.findwise.services.SearchEngine;
import com.findwise.services.SearchEngineImpl;

public class Main {

    public static void main(String[] args) {
        final SearchEngine searchEngine = new SearchEngineImpl();
        DocumentsReader.readIntoMemory(searchEngine);
        DocumentsSearcher.search(searchEngine);
    }

}