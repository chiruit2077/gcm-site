package br.com.ecc.core.mvp.history;

import com.googlecode.gwt.crypto.client.TripleDesCipher;

public class CipherUtil {
	
	private final static TripleDesCipher cipher = new TripleDesCipher();

	static {
		String sKey = "937847375675950209873265"; //TODO levar para o servidor??
		byte[] KEY = new byte[24];
		for(int i=0; i < sKey.length(); i++) {
			Character c = sKey.charAt(i);
			KEY[i] = Byte.decode( c.toString() );
		}
		cipher.setKey(KEY);
	}
	
	public static String encodeToken(String token) {
		if (token != null) {
			try {
				return cipher.encrypt(token);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public static String decodeToken(String token) {
		if (token != null) {
			try {
				return cipher.decrypt( token );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}

}
