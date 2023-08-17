package net.scanner.util;

import net.scanner.Main;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static net.scanner.Main.*;
import static net.scanner.util.FolderChecker.isOnModrinth;
import static net.scanner.util.FolderChecker.isVerified;
import static net.scanner.util.ModList.ModInfo.*;
import static net.scanner.util.UnicodeDetector.containsUnicode;

public class OutputUtil {
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
    public static void scanFile(File file) throws IOException, NoSuchAlgorithmException {
        if (file.getName().endsWith(".jar")) {
            ModList.scanMods(file);
            /*
              # Feather has some type of hash checksum, so it shouldn't be possible to disguise cheats as feather (I think)
            */
            if (!isVerified(file)) {
                output.add("\n" + Main.Cyan + file.getAbsolutePath() + Main.Default);
                output.add(getModName(file) + " |-| " + getModId(file) + " |-| " + getVersion(file));
                output.add("Main Entrypoints: " + Gray + getMainEntrypoints(file) + White);
                output.add("Client Entrypoints: " + Gray + getClientEntrypoints(file) + White);
                output.add("Contains Unicode: " + Gray + ColorBool(containsUnicode(file)) + White);
                try {
                    output.add("Modrinth Verified: " + ColorBool(isVerified(file)));
                    output.add("Mod on Modrinth: " + ColorBool(isOnModrinth(file)));
                } catch (IOException | NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public static String ColorBool(boolean value) {
        if (value) {
            return Main.Green + "true" + Main.Default;
        } else {
            return Main.Red + "false" + Main.Default;
        }
    }
}
