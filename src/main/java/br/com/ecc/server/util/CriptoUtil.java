package br.com.ecc.server.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.net.util.Base64;

public class CriptoUtil {
	 private static byte[] linebreak = {}; // Remove Base64 encoder default linebreak
	 private static String secret = "www.ecc.com.br:x"; // secret key length must be 16
	 private static SecretKey key;
	 private static Cipher cipher;
	 private static Base64 coder;

	 static {
		 try {
		     key = new SecretKeySpec(secret.getBytes(), "AES");
		     cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
		     coder = new Base64(32,linebreak,true);
		 } catch (Throwable t) {
		     t.printStackTrace();
		 }
	 }

	 public static synchronized String Codifica(String plainText) throws Exception {
	        cipher.init(Cipher.ENCRYPT_MODE, key);
	        byte[] cipherText = cipher.doFinal(plainText.getBytes());
	        return  new String(coder.encode(cipherText));
	 }

	 public static synchronized String Decodifica(String codedText) throws Exception {
	        byte[] encypted = coder.decode(codedText.getBytes());
	        cipher.init(Cipher.DECRYPT_MODE, key);
	        byte[] decrypted = cipher.doFinal(encypted);  
	        return new String(decrypted);
	 }
}