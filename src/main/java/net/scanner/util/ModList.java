package net.scanner.util;

import net.scanner.Main;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ModList {
    public static ArrayList<ModInfo> modInformation = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        scanMods();
        for (ModInfo modInfo : modInformation) {
            Main.log("Mod Name: " + modInfo.name);
            Main.log("Mod ID: " + modInfo.id);
            Main.log("Mod Version: " + modInfo.version);
            Main.log("Main Entrypoints: " + modInfo.mainEntrypoints);
            Main.log("Client Entrypoints: " + modInfo.clientEntrypoints + "\n");
        }
    }

    public static void scanMods() {
        //Intellij really must want Objects.requireNonNull..
        for (File file : Path.of(Main.UserFolder + "\\AppData\\Roaming\\.minecraft\\mods").toFile().listFiles())  {
            if (!file.getName().endsWith(".jar")) {
                return;
            }
            try (ZipFile zipFile = new ZipFile(file)) {
                ZipEntry entry = zipFile.getEntry("fabric.mod.json");
                if (entry != null) {
                    byte[] ByteData = zipFile.getInputStream(entry).readAllBytes();
                    String data = new String(ByteData, StandardCharsets.UTF_8);

                    JSONObject object = new JSONObject(data);

                    String modId = object.optString("id", "Unknown");
                    String modName = object.optString("name", "Unknown");
                    String modVersion = object.optString("version", "Unknown");
                    String mainEntrypoint = "None";
                    String clientEntrypoints = "None";

                    JSONObject entryPointsObject = object.optJSONObject("entrypoints");

                    if (entryPointsObject != null) {
                        JSONArray mainArray = entryPointsObject.optJSONArray("main");
                        JSONArray clientArray = entryPointsObject.optJSONArray("client");
                        if (mainArray != null && mainArray.length() > 0) {
                            mainEntrypoint = mainArray.optString(0, "Unknown");
                        } else if (clientArray != null && clientArray.length() > 0) {
                            clientEntrypoints  = clientArray.optString(0, "Unknown");
                        }
                    } else {
                        Main.log( modName + " contains no client or main entrypoints. (Check if it's a library or not)\n");
                    }
                    ModInfo modInfo = new ModInfo(modName, modId, modVersion, mainEntrypoint, clientEntrypoints);
                    modInformation.add(modInfo);
                } else {
                    Main.log(file.getAbsolutePath() + " is missing fabric.mod.json.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static class ModInfo {
        public String name;
        public String id;
        public String version;
        public String mainEntrypoints;
        public String clientEntrypoints;

        public ModInfo(String name, String id, String version, String mainEntrypoints, String clientEntrypoints) {
            this.name = name;
            this.id = id;
            this.version = version;
            this.mainEntrypoints = mainEntrypoints;
            this.clientEntrypoints = clientEntrypoints;
        }
    }
}
