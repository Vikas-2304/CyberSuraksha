package com.example.cybersuraksha;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class ScannerUtils {
    public static String getSHA256(String filePath) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            InputStream is = new FileInputStream(filePath);
            byte[] buffer = new byte[8192];
            int read;
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] hash = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return null;
        }
    }
}