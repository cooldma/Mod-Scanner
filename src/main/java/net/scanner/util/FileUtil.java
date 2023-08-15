package net.scanner.util;

import java.io.File;

public class FileUtil {
    public static boolean isModifiedRecently(File file) {
        if (file.lastModified() <= (System.currentTimeMillis() - 86400/* Past 24 Hours*/)) {
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
