package net.scanner.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ModList {
    public static ArrayList<ModInfo> modInformation = new ArrayList<>();
    public static ArrayList<String> nullEntrypoint = new ArrayList<>();

    public static void scanMods(File file) {
        if (file.getName().endsWith(".jar")) {
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
                        clientEntrypoints = clientArray.optString(0, "Unknown");
                    }
                } else {
                    nullEntrypoint.add(modName + " contains no client or main entrypoints.");
                }

                ModInfo modInfo = new ModInfo(modName, modId, modVersion, mainEntrypoint, clientEntrypoints, file.getPath());
                modInformation.add(modInfo);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class ModInfo {
        public String name;
        public String id;
        public String version;
        public String mainEntrypoints;
        public String clientEntrypoints;
        public String filePath;

        public ModInfo(String name, String id, String version, String mainEntrypoints, String clientEntrypoints, String filePath) {
            this.name = name;
            this.id = id;
            this.version = version;
            this.mainEntrypoints = mainEntrypoints;
            this.clientEntrypoints = clientEntrypoints;
            this.filePath = filePath;
        }

        public static String getModId(File file) {
            ModInfo modInfo = findModInfo(file);
            return modInfo != null ? modInfo.id : "Unknown";
        }

        public static String getModName(File file) {
            ModInfo modInfo = findModInfo(file);
            return modInfo != null ? modInfo.name : "Unknown";
        }

        public static String getVersion(File file) {
            ModInfo modInfo = findModInfo(file);
            return modInfo != null ? modInfo.version : "Unknown";
        }

        public static String getMainEntrypoints(File file) {
            ModInfo modInfo = findModInfo(file);
            return modInfo != null ? modInfo.mainEntrypoints : "None";
        }

        public static String getClientEntrypoints(File file) {
            ModInfo modInfo = findModInfo(file);
            return modInfo != null ? modInfo.clientEntrypoints : "None";
        }

        private static ModInfo findModInfo(File file) {
            String filePath = file.getAbsolutePath();
            for (ModInfo modInfo : modInformation) {
                if (filePath.equals(modInfo.filePath)) {
                    return modInfo;
                }
            }
            return null;
        }
    }
}
