package net.scanner.util;

import java.io.File;
import java.nio.file.Path;

public class RecycleBin {
    private static final Path recycleBin = Path.of("C:\\$Recycle.Bin");
    public static boolean isModified() {
        for (File file : recycleBin.toFile().listFiles()) {
            return FileUtil.isModifiedRecently(file);
        }
        return false;
    }
}
