package com.findwise.exceptions;

public class NoSuchInputException extends Exception {
    private static final String MESSAGE = "There is no such word in given documents.";

    public NoSuchInputException() {
        super(MESSAGE);
    }
}
