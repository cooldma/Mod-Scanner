package net.scanner.util;

import net.scanner.Main;

import java.io.*;

public class FolderChecker {
    private static int suspiciousModCount = 0;
    private static final String modrinthAPI = "https://api.modrinth.com/v2/version_file/";
    public static void main(String[] args) {
        Main.log("Mods may be flagged as suspicious due to not being on modrinth, ");
        Main.log("This tool is only meant to narrow down suspicious mods.");
        Main.log("----------------------------------------------------------------");
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
                                Main.log("Suspicious mod found: " + modFile.getAbsolutePath());
                            }
                        } else {
                            Main.log("Suspicious mod found: " + modFile.getAbsolutePath());
                            suspiciousModCount++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Main.log("No .jar files found in the mods directory.");
            }
        } else {
            Main.log("No mods directory found.");
        }
        if (suspiciousModCount == 0) {
            Main.log("No suspicious mods found within " + modsDirectory + ".");
        }
        Main.log("----------------------------------------------------------------");
        if (suspiciousModCount != 0) {
            Main.log("Use https://github.com/Col-E/Recaf to decompile them.");
        }
    }
}