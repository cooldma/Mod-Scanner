package net.scanner;

import net.scanner.util.*;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static net.scanner.util.OutputUtil.listToString;
import static net.scanner.util.OutputUtil.scanFile;

public class Main {
    public static List<String> output = new ArrayList<>();
    public static final String UserFolder = System.getProperty("user.home");
    public static String mcDir = UserFolder + "\\AppData\\Roaming\\.minecraft";
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        getMC();
        System.out.println();
        System.out.println("Doomsday: " + DoomsdayDetector.recentDoomsday() + ", ! This is NOT 100% accurate !");
        System.out.println("Recently Modified Recycle Bin: " + RecycleBin.isModified());

        scanFile(Path.of(mcDir + "\\mods"));

        System.out.println(listToString(Main.output, "\n"));

        System.out.println("\n! Use RECAF to decompile any flagged mods !\n");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Press enter to exit..");
        scanner.nextLine();
        System.exit(0);
    }
    public static void getMC() {
        //Check if the main .minecraft directory had a recent log modification in the past hour (to verify if that is the directory being used).
        if (!FileUtil.isModifiedRecently(new File(mcDir + "\\logs\\latest.log"), 1)) {
            //If not were going to automatically assume that they could be using feather client,
            try {
                JSONTokener tokener = new JSONTokener(new FileReader(UserFolder + "\\AppData\\Roaming\\.feather\\settings.json"));
                JSONObject obj = new JSONObject(tokener);
                Path fMC = Path.of(obj.optString("mcPath") + "\\logs");
                for (File v : fMC.toFile().listFiles()) {
                    if (v.getName().equals("latest.log") && FileUtil.isModifiedRecently(v, 1)) {
                        mcDir = String.valueOf(fMC.getParent());
                    }
                }
            } catch (FileNotFoundException ignored) {}
        }
    }
}