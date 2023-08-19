package net.scanner.util;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static net.scanner.Main.*;
import static net.scanner.util.Modrinth.*;
import static net.scanner.util.UnicodeDetector.containsUnicode;

public class OutputUtil {

    public static final String Default = "\u001B[0m";
    public static final String Green = "\u001B[32m";
    public static final String Red = "\u001B[31m";
    public static final String White = "\u001B[97m";
    public static final String Cyan = "\u001B[96m";
    public static final String Pink = "\u001B[95m";

    public static String listToString(List<String> list, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }
    public static String ColorBoolean(boolean value) {
        if (value) {
            return Green + "true" + Default;
        } else {
            return Red + "false" + Default;
        }
    }
    public static void scanFile(File file) throws IOException, NoSuchAlgorithmException {
        if (file.getName().endsWith(".jar")) {
            if (!hashVerified(file)) {
                output.add("\n" + Cyan + file.getAbsolutePath() + Default);
                if (containsUnicode(file)) {
                    output.add(White + "Unicode File Names");
                }
                try {
                    output.add("Mod on Modrinth: " + ColorBoolean(isOnModrinth(file)));
                } catch (IOException | NoSuchAlgorithmException ignored) {}
            }
        }
    }
}
