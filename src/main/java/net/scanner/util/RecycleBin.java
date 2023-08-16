package net.scanner.util;

import java.io.File;
import java.nio.file.Path;

public class RecycleBin {
    private static final Path recycleBin = Path.of("C:\\$Recycle.Bin");
    public static boolean isModified() {
        for (File file : recycleBin.toFile().listFiles()) {
            if (file.lastModified() != 0 && FileUtil.isModifiedRecently(file, 24)) {
                return true;
            }
        }
        return false;
    }
}
