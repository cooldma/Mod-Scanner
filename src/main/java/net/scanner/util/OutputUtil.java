package net.scanner.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static net.scanner.Main.output;
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
    public static void scanFile(Path path) throws IOException, NoSuchAlgorithmException {
        for (File file : path.toFile().listFiles()) {
            if (file.getName().endsWith(".jar")) {
                ModList.scanMods(file);
                //Feather has some type of hash checksum, so it shouldn't be possible to disguise cheats as feather (I think)
                if (getModId(file) != "feather") {
                    if (!isVerified(file)) {
                        output.add("\n" + file.getAbsolutePath());
                        output.add(getModName(file) + " |-| " + getModId(file) + " |-| " + getVersion(file));
                        output.add("Main Entrypoints: " + getMainEntrypoints(file));
                        output.add("Client Entrypoints: " + getClientEntrypoints(file));
                        output.add("Contains Unicode: " + containsUnicode(file));
                        try {
                            output.add("Modrinth Verified: " + isVerified(file));
                            output.add("Mod on Modrinth: " + isOnModrinth(file));
                        } catch (IOException | NoSuchAlgorithmException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }
}
