package utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 *
 * @author Omar
 */
public class Utils {
    
    /**
     * Generates a random 6-character alphanumeric code.
     * @return a 6-character alphanumeric string.
     */
    public static String generateRandomCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }

        return code.toString();
    }
    
    
        private String generateSecureVerificationCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    
    
}
