package ru.kpfu.itis.gnt.models;

import ru.kpfu.itis.gnt.exceptions.MessageException;
import ru.kpfu.itis.gnt.utils.StringConverter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Message {

    public static final int MAX_LENGTH = 65356;
    protected static final byte[] START_BYTES = new byte[]{0xA, 0xB};
    protected final byte[] data;
    protected final MessageType type;


    protected Message(MessageType type, byte[] data) {
        this.data = data;
        this.type = type;
    }

    public static Message readMessage(InputStream inputStream) throws IOException, MessageException {
        byte[] buffer = new byte[MAX_LENGTH];
        inputStream.read(buffer, 0, START_BYTES.length);
        if (!Arrays.equals(
                Arrays.copyOfRange(buffer, 0, START_BYTES.length),
                START_BYTES
        )) {
            throw new MessageException("Message first bytes must be:" + Arrays.toString(START_BYTES));
        }
        inputStream.read(buffer, 0, 4);
        int messageType = ByteBuffer.wrap(buffer, 0, 4).getInt();
        checkType(messageType);
        inputStream.read(buffer, 0, 4);
        int messageLength = ByteBuffer.wrap(buffer, 0, 4).getInt();
        checkLength(messageLength);
        inputStream.read(buffer, 0, messageLength);
        return new Message(MessageType.values()[messageType], Arrays.copyOfRange(buffer, 0, messageLength));

    }

    public static Message createMessage(MessageType messageType, byte[] data) throws MessageException {
        checkLength(data.length);
        checkType(messageType.getType());
        return new Message(messageType, data);
    }

    private static void checkType(int type) throws MessageException {
        boolean isSupported = false;
        for (MessageType typeEnum : MessageType.values()) {
            if (typeEnum.getType() == type) {
                isSupported = true;
                break;
            }
        }
        if (!isSupported) {
            throw new MessageException("This type of message is not supported");
        }
    }

    private static void checkLength(int length) throws MessageException {
        if (length > MAX_LENGTH) {
            throw new MessageException("The length of your message has exceeded the limit");
        }
    }

    public static String toString(Message message) throws UnsupportedEncodingException, MessageException {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = Message.getBytes(message);

        byte[] data = Arrays.copyOfRange(bytes, 10, bytes.length);
        sb.append(StringConverter.decode(data));
        return sb.toString();
    }

    public static InputStream getImageInputStream(Message message) throws MessageException {
        byte[] bytes = Message.getBytes(message);
        return new ByteArrayInputStream(Arrays.copyOfRange(bytes, 10, bytes.length));
    }

    public static byte[] getBytes(Message message) throws MessageException {
        checkLength(message.data.length);
        checkType(message.getType());
        int mMessageLength = START_BYTES.length
                + 4 + 4
                + message.getData().length;

        byte[] mMessage = new byte[mMessageLength];
        int j = 0;
        for (byte startByte : START_BYTES) {
            mMessage[j++] = startByte;
        }

        byte[] type = ByteBuffer.allocate(4).putInt(message.getType()).array();

        for (byte value : type) {
            mMessage[j++] = value;
        }

        byte[] length = ByteBuffer.allocate(4).putInt(message.getData().length).array();
        for (byte b : length) {
            mMessage[j++] = b;
        }

        byte[] data = message.getData();
        for (byte datum : data) {
            mMessage[j++] = datum;
        }
        return mMessage;
    }

    public int getType() {
        return type.getType();
    }

    public byte[] getData() {
        return data;
    }


}
