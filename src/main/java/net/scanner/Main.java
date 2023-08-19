package net.scanner;

import net.scanner.util.TimeUtil;
import org.fusesource.jansi.AnsiColors;
import org.fusesource.jansi.AnsiConsole;
import org.json.*;

import java.io.*;
import java.util.*;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

import static net.scanner.util.OutputUtil.*;

public class Main {
    public static List<String> output = new ArrayList<>();
    public static final String UserFolder = System.getProperty("user.home");
    public static String mcDir = UserFolder + "\\AppData\\Roaming\\.minecraft";
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        AnsiConsole.systemInstall();
        getMcDirectory();

        System.out.println(Green + "\nScanning: " + mcDir + "\n" + White);

        System.out.println("Recently Modified Recycle Bin: " + ColorBoolean(TimeUtil.isRecycleBinModified()) + White);

        Path scanDir = Path.of(mcDir + "\\mods");
        for (File file : scanDir.toFile().listFiles()) {
            scanFile(file);
        }

        System.out.println(listToString(Main.output, "\n"));

        System.out.println(Pink + "\n! Use RECAF to decompile any flagged mods !\n" + Cyan + "https://github.com/Col-E/Recaf\n" + White);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Press enter to exit..");
        scanner.nextLine();
        System.exit(0);
    }
    public static void getMcDirectory() {
        //Check if the main .minecraft directory had a recent log modification in the past hour (to verify if that is the directory being used).
        if (!TimeUtil.isModifiedRecently(new File(mcDir + "\\logs\\latest.log"), 1)) {
            //If not were going to automatically assume that they could be using feather client,
            try {
                JSONTokener tokener = new JSONTokener(new FileReader(UserFolder + "\\AppData\\Roaming\\.feather\\settings.json"));
                JSONObject obj = new JSONObject(tokener);
                Path featherMC = Path.of(obj.optString("mcPath") + "\\logs");
                for (File v : featherMC.toFile().listFiles()) {
                    if (v.getName().equals("latest.log") && TimeUtil.isModifiedRecently(v, 1)) {
                        mcDir = String.valueOf(featherMC.getParent());
                    }
                }
            } catch (FileNotFoundException ignored) {}
        }
    }
}