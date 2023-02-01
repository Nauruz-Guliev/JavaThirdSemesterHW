package ru.kpfu.itis.gnt.utils;

import java.io.UnsupportedEncodingException;

public class StringConverter {
    private final  static String CHARSET = "UTF-8";
    public static byte[] encode(String message) throws UnsupportedEncodingException {
        return message.getBytes(CHARSET);
    }

    public static String decode(byte[] messageBytes) throws UnsupportedEncodingException {
        return new String(messageBytes, CHARSET);
    }
}
