package net.scanner.util;

import java.io.*;
import java.net.*;

public class FolderChecker {
    public static void main(String[] args) {
        try {
            String userHome = System.getProperty("user.home");
            String downloadFolder = userHome + File.separator + "Downloads";
            String outputFilePath = downloadFolder + File.separator + "output.txt";
            PrintStream fileStream = new PrintStream(new FileOutputStream(outputFilePath));
            System.setOut(fileStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int modCount = 0;
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

                        String responseData = fetch("https://api.modrinth.com/v2/version_file/" + sha512Hash + "?algorithm=sha512");
                        if (!responseData.isEmpty()) {
                            if (!responseData.contains("\"id\":")) {
                                System.out.println("Suspicious mod found: " + modFile.getAbsolutePath());
                            }
                        } else {
                            System.out.println("Suspicious mod found: " + modFile.getAbsolutePath());
                            modCount++;
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
        if (modCount == 0) {
            System.out.println("No suspicious mods found within " + modsDirectory + ".");
        }
        System.out.println("----------------------------------------------------------------");
        if (modCount != 0) {
            System.out.println("Use https://github.com/Col-E/Recaf to decompile them.");
        }
        System.exit(0);
    }

    private static String fetch(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }
        } catch (FileNotFoundException e) {
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}