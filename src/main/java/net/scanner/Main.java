package net.scanner;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static net.scanner.util.FileUtil.createDirectory;

public class Main {
    public static final String UserFolder = System.getProperty("user.home"); //users home directory (C:\Users\%username%)
    public static File scannerFile = new File(UserFolder + "\\Downloads\\ModScannerOutput");
    public static final Kernel32 kernel32 = Native.load("kernel32", Kernel32.class);
    public static void main(String[] args) {
        createDirectory(scannerFile);
    }

    public static void log(String msg) {
        System.out.println(msg);
    }

    public static void setSystemOutput(File file) {
        try {
            PrintStream fileStream = new PrintStream(new FileOutputStream(file));
            System.setOut(fileStream);
        } catch (FileNotFoundException e) {}
    }
}