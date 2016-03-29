package com.cmdesign.hellonwsuaf.helper;

//import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;

public class MD5Helper {
	public static String encodeString(String source) {
	    byte[] hash;

	    try {
	        hash = MessageDigest.getInstance("MD5").digest(source.getBytes("UTF-8"));
	    } catch (Exception e) {
	        return null;
	    }

	    StringBuilder hex = new StringBuilder(hash.length * 2);
	    for (byte b : hash) {
	        int i = (b & 0xFF);
	        if (i < 0x10) hex.append('0');
	        hex.append(Integer.toHexString(i));
	    }

	    return hex.toString();	
	}
}
