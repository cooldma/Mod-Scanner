package net.scanner.util;

import java.io.File;
import java.nio.file.Path;

public class TimeUtil {
    public static boolean isModifiedRecently(File file, int hours) {
        if (file.lastModified() >= (System.currentTimeMillis() - ((long) hours * 60 * 60 * 1000))) {
            return true;
        }
        return false;
    }
    public static boolean isRecycleBinModified() {
        Path recycleBin = Path.of("C:\\$Recycle.Bin");
        for (File file : recycleBin.toFile().listFiles()) {
            if (file.lastModified() != 0 && TimeUtil.isModifiedRecently(file, 24)) {
                return true;
            }
        }
        return false;
    }
}
