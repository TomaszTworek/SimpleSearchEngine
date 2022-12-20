package com.findwise.infrastructure;

import com.findwise.exceptions.IncorrectInputException;
import com.findwise.exceptions.NoSuchInputException;
import com.findwise.model.IndexEntry;
import com.findwise.services.SearchEngine;

import java.util.List;
import java.util.Scanner;

public class DocumentsSearcher {

    public static final String ESCAPE_KEY = ":!q";
    public static final String ENTER_TERM = "(:!q - to escape) Enter term: ";

    private DocumentsSearcher() {
    }

    public static void search(SearchEngine searchEngine) {
        while (true) {
            System.out.println(ENTER_TERM);
            final Scanner scanner = new Scanner(System.in);
            final String userInput = scanner.nextLine();
            if (userInput.equals(ESCAPE_KEY)) {
                break;
            }
            try {
                List<IndexEntry> searchResult = searchEngine.search(userInput.toLowerCase());
                System.out.println(searchResult);
            } catch (NoSuchInputException | IncorrectInputException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
