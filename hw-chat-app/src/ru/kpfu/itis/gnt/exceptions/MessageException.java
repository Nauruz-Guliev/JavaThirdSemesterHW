package ru.kpfu.itis.gnt.exceptions;

public class MessageException extends Exception{
    public MessageException(String errorMessage) {
        super(errorMessage);
    }
}
