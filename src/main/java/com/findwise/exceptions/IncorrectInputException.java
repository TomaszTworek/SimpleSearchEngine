package com.findwise.exceptions;

public class IncorrectInputException extends Exception {

    private static final String MESSAGE = "Incorrect input. Expected non empty word.";

    public IncorrectInputException() {
        super(MESSAGE);
    }
}
