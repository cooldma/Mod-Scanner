package net.scanner.util;

import java.io.File;

public class FileUtil {
    public static boolean isModifiedRecently(File file, int hours) {
        if (file.lastModified() >= (System.currentTimeMillis() - ((long) hours * 60 * 60 * 1000))) {
            return true;
        }
        return false;
    }
    public static void createDirectory(File file) {
        if (!file.exists()) {
            file.mkdir();
        } else {file.delete();}
    }
}
