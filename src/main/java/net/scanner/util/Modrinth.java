package net.scanner.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Modrinth {
    public static boolean hashVerified(File modFile) throws IOException, NoSuchAlgorithmException {
        String sha512Hash = calcSHA512(modFile.toPath());
        String hashResponseData = fetch("https://api.modrinth.com/v2/version_file/" + sha512Hash + "?algorithm=sha512");
        if (hashResponseData.isEmpty()) {
            return false;
        }
        return true;
    }
    public static boolean isOnModrinth(File modFile) throws IOException, NoSuchAlgorithmException {
        String modIdResponseData = fetch("https://api.modrinth.com/v2/project/" + getModId(modFile) + "/version");
        if (modIdResponseData.isEmpty()) {
            return false;
        }
        return true;
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
        } catch (IOException e) {return "";}
    }
    public static String getModId(File file) {
        try (ZipFile zipFile = new ZipFile(file)) {
            ZipEntry entry = zipFile.getEntry("fabric.mod.json");
            if (entry != null) {
                byte[] ByteData = zipFile.getInputStream(entry).readAllBytes();
                String data = new String(ByteData, StandardCharsets.UTF_8);

                JSONObject object = new JSONObject(data);

                return object.optString("id", "Unknown");
            }
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    private static String calcSHA512(Path filePath) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
        }
        byte[] hashBytes = md.digest();
        StringBuilder hashString = new StringBuilder();
        for (byte hashByte : hashBytes) {
            hashString.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
        }
        return hashString.toString();
    }
}