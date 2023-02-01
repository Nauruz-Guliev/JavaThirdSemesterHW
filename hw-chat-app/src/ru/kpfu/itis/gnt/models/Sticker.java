package ru.kpfu.itis.gnt.models;

import java.util.Arrays;
import java.util.Objects;

public record Sticker(byte[] stickerBytes, String stickerCode) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sticker sticker)) return false;
        return Arrays.equals(stickerBytes, sticker.stickerBytes) && Objects.equals(stickerCode, sticker.stickerCode);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(stickerCode);
        result = 31 * result + Arrays.hashCode(stickerBytes);
        return result;
    }

}
