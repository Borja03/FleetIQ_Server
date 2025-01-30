package encryption;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;

public class ServerSideDecryption {

	static {
    	Security.addProvider(new BouncyCastleProvider());
	}

	private static final String PRIVATE_KEY_PATH = "encryption/private.key";

	// Decrypts the encrypted data (loads private key internally)
	public static String decrypt(byte[] encryptedData) throws Exception {
    	PrivateKey privateKey = loadPrivateKey();
    	Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
    	cipher.init(Cipher.DECRYPT_MODE, privateKey);
    	return new String(cipher.doFinal(encryptedData));
	}

	// Loads the private key from resources inside the JAR
	private static PrivateKey loadPrivateKey() throws Exception {
    	byte[] keyBytes = readResourceBytes(PRIVATE_KEY_PATH);
    	KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
    	PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
    	return keyFactory.generatePrivate(keySpec);
	}

	// Reads the private key as bytes (supports both JAR and filesystem)
	private static byte[] readResourceBytes(String resourcePath) throws Exception {
    	try (InputStream inputStream = ServerSideDecryption.class.getClassLoader().getResourceAsStream(resourcePath);
         	ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

        	if (inputStream == null) {
            	throw new IllegalArgumentException("Private key resource not found: " + resourcePath);
        	}

        	byte[] buffer = new byte[8192];
        	int bytesRead;
        	while ((bytesRead = inputStream.read(buffer)) != -1) {
            	bos.write(buffer, 0, bytesRead);
        	}
        	return bos.toByteArray();
    	}
	}

}


