package com.example.urlshortener.domain.url.application;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UrlShortener {
    public static String shortenUrl(String fullUrl) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = messageDigest.digest(fullUrl.getBytes());

            StringBuilder shortUrlBuilder = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                int value = hashBytes[i] & 0xFF;
                String hexString = Integer.toHexString(value);
                if (hexString.length() == 1) {
                    shortUrlBuilder.append('0');
                }
                shortUrlBuilder.append(hexString);
            }

            return shortUrlBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception appropriately
            return "Error occurred while shortening the URL.";
        }
    }

}
