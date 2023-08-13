package net.nick.utilxd;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
    public static String calculateSHA512Hash(Path filePath) throws IOException, NoSuchAlgorithmException {
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

    private static String calculateSHA256Hash(Path filePath) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            byte[] buffer = new byte[4096];
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
