package net.scanner.util;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class UnicodeDetector {
    public static boolean containsUnicode(File file) {
        if (file.getName().endsWith(".jar")) {
            try (JarFile jarFile = new JarFile(file)) {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (nonASCII(entry.getName())) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    private static boolean nonASCII(String input) {
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c >= 128) { //For the 128 total ASCII characters
                return true;
            }
        }
        return false;
    }
}
