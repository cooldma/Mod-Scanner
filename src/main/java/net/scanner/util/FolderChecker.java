package net.scanner.util;

import java.io.*;
import java.security.NoSuchAlgorithmException;

public class FolderChecker {
    public static boolean hashVerified(File modFile) throws IOException, NoSuchAlgorithmException {
        String sha512Hash = HashUtil.calcSHA512(modFile.toPath());
        String hashResponseData = HttpsUtil.fetch("https://api.modrinth.com/v2/version_file/" + sha512Hash + "?algorithm=sha512");
        if (hashResponseData.isEmpty()) {
            return false;
        }
        return true;
    }
    public static boolean isOnModrinth(File modFile) throws IOException, NoSuchAlgorithmException {
        String modIdResponseData = HttpsUtil.fetch("https://api.modrinth.com/v2/project/" + ModList.ModInfo.getModId(modFile) + "/version");
        if (modIdResponseData.isEmpty()) {
            return false;
        }
        return true;
    }
}