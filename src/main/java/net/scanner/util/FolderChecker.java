package net.scanner.util;

import java.io.*;
import java.security.NoSuchAlgorithmException;

public class FolderChecker {
    private static final String modrinthAPI = "https://api.modrinth.com/v2/version_file/";
    public static boolean isVerified(File modFile) throws IOException, NoSuchAlgorithmException {
        String sha512Hash = HashUtil.calculateSHA512Hash(modFile.toPath());
        String responseData = HttpsUtil.fetch(modrinthAPI + sha512Hash + "?algorithm=sha512");
        if (responseData.isEmpty()) {
            return false;
        }
        return true;
    }
}