package com.findwise.exceptions;

public class ReadingFileException extends RuntimeException {

    private static final String MESSAGE = "Error while reading a file.";

    public ReadingFileException() {
        super(MESSAGE);
    }
}
