package ru.kpfu.itis.gnt.models;

public enum MessageType {
    USERNAME(0),
    REGULAR_MESSAGE(1),

    STICKER(2),

    ERROR_MESSAGE(3);

    private final int messageType;

    MessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getType() {
        return messageType;
    }

}