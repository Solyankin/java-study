package org.example.utils;

import java.util.Random;

public class StringUtils {


    public static String generateRandomString(Integer length) {
        if (length  == null) {
            return null;
        }
        if (length < 0) {
            throw new IllegalArgumentException("Length must be positive number");
        }


        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
    }
}
