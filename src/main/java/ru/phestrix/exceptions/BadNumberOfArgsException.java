package ru.phestrix.exceptions;

public class BadNumberOfArgsException extends RuntimeException{
    public BadNumberOfArgsException(String message){
        super(message);
    }
}
