package com.findwise.infrastructure;

import com.findwise.exceptions.ReadingFileException;
import com.findwise.services.SearchEngine;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DocumentsReader {

    public static final String DOCUMENTS_PATH = "documents\\";

    private DocumentsReader() {
    }

    public static void readIntoMemory(SearchEngine searchEngine) {
        final Path path = Paths.get(DOCUMENTS_PATH);
        try (Stream<Path> stream = Files.list(path)) {
            stream.forEach(file -> {
                String content;
                try {
                    content = readFile(DOCUMENTS_PATH + file.getFileName(), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    throw new ReadingFileException();
                }
                searchEngine.indexDocument(String.valueOf(file.getFileName()), content);
            });
        } catch (IOException e) {
            throw new ReadingFileException();
        }
    }

    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
