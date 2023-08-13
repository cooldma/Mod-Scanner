package net.scanner;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;

import java.io.File;

public class Main {
    public static final String userhome = System.getProperty("user.home"); //C:\Users\*user*
    public static final String downloadsFolder = userhome + File.separator + "Downloads"; // C:\Users\*user*\Downloads
    public static final String MinecraftDir = userhome + "\\AppData\\Roaming\\.minecraft"; // C:\Users\*user*\AppData\Roaming\.minecraft
    public static final String FeatherDir = userhome + "\\AppData\\Roaming\\.feather"; // C:\Users\*user*\AppData\Roaming\.feather
    public static final String modsFolder = MinecraftDir + "\\mods"; // .minecraft/mods
    public static final Kernel32 kernel32 = Native.load("kernel32", Kernel32.class);
    public static void main(String[] args) {

    }
}