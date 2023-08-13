package net.scanner.util;

import java.io.File;

public class FileUtil {
    private static boolean isModifiedRecently(File file) {
        long currentTime = System.currentTimeMillis();
        long fileLastModified = file.lastModified();
        return (currentTime - fileLastModified) <= 24 * 60 * 60 * 1000; // modified within the past 24 hours
    }
}
