package org.lemanoman.crypts;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
 
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
 
/**
 * A utility class that encrypts or decrypts a file.
 * @author www.codejava.net
 *
 */
public class CryptoUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    private static String key = "Hello world :)  ";
 
    public static void encrypt(File inputFile, File outputFile) throws CryptoException {
	doCryptoLessMem(Cipher.ENCRYPT_MODE,inputFile, outputFile);
    }
 
    public static void decrypt(File inputFile, File outputFile)
            throws CryptoException {
	doCryptoLessMem(Cipher.DECRYPT_MODE,inputFile, outputFile);
    }
 
    private static void doCrypto(int cipherMode, File inputFile,
            File outputFile) throws CryptoException {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);
             
            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);
             
            byte[] outputBytes = cipher.doFinal(inputBytes);
             
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);
             
            inputStream.close();
            outputStream.close();
             
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }
    
    private static void doCryptoLessMem(int cipherMode, File inputFile,
            File outputFile) throws CryptoException {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);
             
            FileInputStream inputStream = new FileInputStream(inputFile);
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            CipherOutputStream output = new CipherOutputStream(outputStream,cipher);
            
            byte[] buffer = new byte[8192];
            int count;
            while ((count = inputStream.read(buffer)) > 0)
            {
        	output.write(buffer, 0, count);
                
            }
            
            inputStream.close();
            outputStream.close();
            output.close();
             
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }
}