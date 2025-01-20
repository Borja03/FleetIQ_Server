/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Omar
 */
public class HashMD5 {
       public static String hashText(String text) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md5.digest(text.getBytes());

            // Convert the byte array to a hexadecimal representation
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = String.format("%02X", b);
                hexStringBuilder.append(hex);
            }

            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String inputText = "Hello, MD5!";
        String md5Hash = hashText(inputText);
        System.out.println("MD5 Hash: " + md5Hash);
    }
}
