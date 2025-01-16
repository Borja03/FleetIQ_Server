/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

import javax.crypto.Cipher;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class ServidorAsimetricPrivate {

    public PrivateKey loadPrivateKey(String resourcePath) throws Exception {
        byte[] privateKeyBytes;
        try (InputStream keyInputStream = getClass().getResourceAsStream(resourcePath);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            if (keyInputStream == null) {
                throw new FileNotFoundException("No se encontró el archivo de clave privada.");
            }
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = keyInputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            privateKeyBytes = baos.toByteArray();
        }

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return keyFactory.generatePrivate(privateKeySpec);
    }

    public byte[] decryptMessage(byte[] encryptedData, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encryptedData);
    }

    public void startServer(int port, PrivateKey privateKey) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor escuchando en el puerto " + port + "...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     InputStream inputStream = clientSocket.getInputStream();
                     ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        baos.write(buffer, 0, bytesRead);
                    }
                    byte[] encryptedData = baos.toByteArray();

                    byte[] decryptedData = decryptMessage(encryptedData, privateKey);

                    System.out.println("Mensaje recibido del cliente: " + new String(decryptedData));
                } catch (Exception e) {
                    System.err.println("Error al manejar una conexión del cliente: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}
