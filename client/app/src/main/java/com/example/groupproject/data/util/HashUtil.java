package com.example.groupproject.data.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    private HashUtil() {}

    public static String md5(String password) {
        MessageDigest messageDigest;
        byte[] passwordHash;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());
            passwordHash = messageDigest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < passwordHash.length; i++) {
                String num = String.format("%02X", 0xFF & passwordHash[i]);
                hexString.append(num.toLowerCase());
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
