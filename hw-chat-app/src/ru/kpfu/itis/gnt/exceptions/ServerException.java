package ru.kpfu.itis.gnt.exceptions;

public class ServerException extends Exception{
    public ServerException(String errorMessage) {
        super(errorMessage);
    }
}
