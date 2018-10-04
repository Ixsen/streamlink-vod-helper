package de.ixsen.streamlinkvodhelper.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CacheUtils implements LoggerHelper {
    private static final String CACHE_FOLDER = "cache";

    public static void initialize() {
        File file = new File(CACHE_FOLDER);
        if (!file.exists()) {
            if (file.mkdir()) {
                LoggerHelper.getLogger().info("Cache folder created");
            } else {
                LoggerHelper.getLogger().severe("Could not create cache folder, this may cause issues");
            }
        }
    }

    public static String cacheImage(String url) {
        String fileName = url.replace("https://static-cdn.jtvnw.net/", "");
        fileName = fileName.replace('/', '.');

        File file = new File(getFilePath(fileName));
        if (!file.exists()) {
            try {
                BufferedImage image = ImageIO.read(new URL(url));
                ImageIO.write(image, "jpg", file);
                LoggerHelper.getLogger().info("Cached new file: " + fileName);
            } catch (IOException e) {
                LoggerHelper.getLogger().severe("Could not cache thumbnail, images may not be shown");
            }
        }

        return file.toURI().toString();
    }

    private static String getFilePath(String fileName) {
        return CACHE_FOLDER + "/" + fileName;
    }
}
