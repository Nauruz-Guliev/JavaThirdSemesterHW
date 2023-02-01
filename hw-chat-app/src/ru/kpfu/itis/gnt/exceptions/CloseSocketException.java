package ru.kpfu.itis.gnt.exceptions;

public class CloseSocketException extends Exception{
    public CloseSocketException(String errorMessage) {
        super(errorMessage);
    }
}