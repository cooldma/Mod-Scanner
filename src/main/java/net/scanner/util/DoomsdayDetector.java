package net.scanner.util;

import java.io.File;
import java.nio.file.Path;

public class DoomsdayDetector {
    private static final Path oraclePath = Path.of("C:\\ProgramData\\Oracle\\Java\\.oracle_jre_usage");

    public static boolean recentDoomsday() { //This is not 100% accurate
        for (File file : oraclePath.toFile().listFiles()) {
            if (FileUtil.isModifiedRecently(file, 12)) {
                return true;
            }
        }
        return false;
    }
}
