package ru.kpfu.itis.gnt.repository;

import ru.kpfu.itis.gnt.exceptions.RepositoryException;
import ru.kpfu.itis.gnt.models.Message;
import ru.kpfu.itis.gnt.models.MessageType;
import ru.kpfu.itis.gnt.models.Sticker;
import ru.kpfu.itis.gnt.utils.ImageConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class StickerRepository {


    // по сути код стикера здесь дублируется, но мне нужен был быстрый доступ к значению по ключу-коду стикера
    private final static HashMap<String, Sticker> stickersMap = new HashMap<>();

    private static final String STICKER_TEMPLATE_SIGN = "@";

    private static final String[] stickerPaths = new String[]{
            "src/stickers/sticker.png",
            "src/stickers/sticker2.png"
    };

    private static final String[] stickerCodes = new String[stickerPaths.length];
    static {
        try {
            initRepository();
        } catch (IOException e) {
            throw new RuntimeException("Unable to initialize repository due to: " + e.getMessage());
        }
    }


    public static void initRepository() throws IOException {
        for (int i = 0; i < stickerPaths.length; i++) {
            byte[] stickerBytes = ImageConverter.convertImageToBytes(stickerPaths[i]);
            StringBuilder stickerCode = new StringBuilder();
            stickerCode.append(STICKER_TEMPLATE_SIGN).append(i).append(i).append(STICKER_TEMPLATE_SIGN);
            stickersMap.put(stickerCode.toString(), new Sticker(stickerBytes, stickerCode.toString()));
            stickerCodes[i] = stickerCode.toString();
        }
    }


    public static String[] getStickerCodes() throws RepositoryException {
        for (String code : stickerCodes) {
            if (Objects.equals(code, "0") || (Objects.equals(code, ""))) {
                throw new RepositoryException("Stickers have not been initialized by server");
            }
        }
        return stickerCodes;
    }

    public static Sticker getSticker(String stickerCode) throws RepositoryException {
        for (String stickerKey : stickersMap.keySet()) {
            if (stickerKey.equals(stickerCode)) {
                return stickersMap.get(stickerKey);
            }
        }
        throw new RepositoryException("Sticker with such a code does not exist");
    }
}
