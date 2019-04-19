package org.lemanoman.crypts;
import java.io.File;
 
/**
 * A tester for the CryptoUtils class.
 * @author www.codejava.net
 *
 */
public class CryptoUtilsTest {
    public static void main(String[] args) {
        String key = "Mary has one cat";
        key = "Hello world :)  ";
        File inputFile = new File("/home/kevim/posts-2016-08-25.txt");
        File encryptedFile = new File("/home/kevim/posts-2016-08-25.encrypted");
        File decryptedFile = new File("/home/kevim/posts-2016-08-25.decrypted");
         
        try {
            CryptoUtils.encrypt(inputFile, inputFile);
            CryptoUtils.decrypt(encryptedFile, decryptedFile);
        } catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}