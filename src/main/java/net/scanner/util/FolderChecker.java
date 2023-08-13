package net.scanner.util;

import net.scanner.Main;

import java.io.*;

public class FolderChecker {
    private static int suspiciousModCount = 0;
    private static final String modrinthAPI = "https://api.modrinth.com/v2/version_file/";
    private static final String outputFilePath = Main.downloadsFolder + "\\output.txt";
    public static void main(String[] args) {
        try {
            PrintStream fileStream = new PrintStream(new FileOutputStream(outputFilePath));
            System.setOut(fileStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Mods may be flagged as suspicious due to not being on modrinth, ");
        System.out.println("This tool is only meant to narrow down suspicious mods.");
        System.out.println("----------------------------------------------------------------");
        File modsDirectory = new File(System.getenv("APPDATA") + "/.minecraft/mods");
        if (modsDirectory.exists() && modsDirectory.isDirectory()) {
            File[] modFiles = modsDirectory.listFiles((dir, name) -> name.endsWith(".jar"));
            if (modFiles != null && modFiles.length > 0) {
                for (File modFile : modFiles) {
                    try {
                        String sha512Hash = HashUtil.calculateSHA512Hash(modFile.toPath());

                        String responseData = HttpsUtil.fetch(modrinthAPI + sha512Hash + "?algorithm=sha512");
                        if (!responseData.isEmpty()) {
                            if (!responseData.contains("\"id\":")) {
                                System.out.println("Suspicious mod found: " + modFile.getAbsolutePath());
                            }
                        } else {
                            System.out.println("Suspicious mod found: " + modFile.getAbsolutePath());
                            suspiciousModCount++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("No .jar files found in the mods directory.");
            }
        } else {
            System.out.println("No mods directory found.");
        }
        if (suspiciousModCount == 0) {
            System.out.println("No suspicious mods found within " + modsDirectory + ".");
        }
        System.out.println("----------------------------------------------------------------");
        if (suspiciousModCount != 0) {
            System.out.println("Use https://github.com/Col-E/Recaf to decompile them.");
        }
    }
}