package ru.kpfu.itis.gnt.exceptions;

public class ClientException extends Exception{
    public ClientException(String errorMessage) {
        super(errorMessage);
    }
}
