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
    public static ArrayList modInfo = new ArrayList();
    public static ArrayList modEntrypoints = new ArrayList();
    public ModList() {
        gatherInfo();
    }
    private static void gatherInfo() {
        for (File file : Path.of(Main.modsFolder).toFile().listFiles()) {
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
                    }
                    modInfo.add(modName + " " + modId);
                    modEntrypoints.add(modName + " main: " + mainEntrypoint + " client: " + clientEntrypoints);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
