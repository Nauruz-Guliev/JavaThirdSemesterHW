package ru.kpfu.itis.gnt.utils;

import ru.kpfu.itis.gnt.exceptions.RepositoryException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageConverter {

    private static final String IMAGE_FORMAT = "png";

    // пусть репозиторий отслеживает случай, когда не получилось преобразовать в байты
    public static byte[] convertImageToBytes(String filePath) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(filePath));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(originalImage, IMAGE_FORMAT, baos);
        return baos.toByteArray();
    }
}
