package de.ixsen.streamlinkvodhelper.utils;

import java.io.File;

public class CacheUtils implements LoggerHelper {
    private static final String CACHE_FOLDER = "cache";

    public static void initialize() {
        File file = new File(CACHE_FOLDER);
        if (!file.exists() || !file.isFile()) {
            if (file.mkdir()) {
                LoggerHelper.getLogger().info("Cache folder created");
            } else {
                LoggerHelper.getLogger().severe("Could not create cache folder, this may cause issues");
            }
        }
    }

    public static String cacheImage(String url) {
        return url;
    }

    public static void getImage(String name) {

    }
}
