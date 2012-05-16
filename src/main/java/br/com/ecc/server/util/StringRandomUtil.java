package br.com.ecc.server.util;

import java.util.Random;

public class StringRandomUtil {

	private static final char[] chars = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'x', 'y', 'z', 'w'};

	public static String randomString(int length){
		Random r = new Random();
		StringBuilder sb = new StringBuilder(length);
		
		char c;
		String upper;
		int next;
		for (int i = 0; i < length; i++) {
			r.setSeed(System.nanoTime());
			next = r.nextInt(chars.length);
			c = chars[next];
			if (next%2==0){
				upper = (""+c).toUpperCase();
			} else {
				upper = ""+c;
			}
			sb.append(upper);
		}
		
		return sb.toString();
	}
	
}
