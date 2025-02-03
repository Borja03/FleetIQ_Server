package encryption;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class ServerSideDecryption {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String PRIVATE_KEY_PATH = "encryption/private.key";

    // Decrypts a Base64 encoded encrypted string
    public static String decrypt(String encryptedBase64String) throws Exception {
        // Step 1: Get Base64 decoder instance
        Base64.Decoder decoder = Base64.getDecoder();
        
        // Step 2: Convert Base64 string back to encrypted bytes
        byte[] encryptedBytes = decoder.decode(encryptedBase64String);
        
        // Step 3: Load the private key
        PrivateKey privateKey = loadPrivateKey();
        
        // Step 4: Initialize cipher for decryption
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        
        // Step 5: Decrypt the data
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        
        // Step 6: Convert decrypted bytes to string
        String decryptedString = new String(decryptedBytes);
        
        return decryptedString;
    }

    // Loads the private key from resources inside the JAR
    private static PrivateKey loadPrivateKey() throws Exception {
        // Step 1: Read the private key file
        byte[] keyBytes = readResourceBytes(PRIVATE_KEY_PATH);
        
        // Step 2: Create RSA key factory
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        
        // Step 3: Create private key specification
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        
        // Step 4: Generate private key
        return keyFactory.generatePrivate(keySpec);
    }

    // Reads the private key as bytes (supports both JAR and filesystem)
    private static byte[] readResourceBytes(String resourcePath) throws Exception {
        try (InputStream inputStream = ServerSideDecryption.class.getClassLoader().getResourceAsStream(resourcePath);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            
            // Check if resource exists
            if (inputStream == null) {
                throw new IllegalArgumentException("Private key resource not found: " + resourcePath);
            }

            // Read resource data in chunks
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            
            return bos.toByteArray();
        }
    }
}