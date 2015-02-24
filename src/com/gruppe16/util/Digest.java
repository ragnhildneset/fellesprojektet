package com.gruppe16.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class Digest {
	
	public static String getHash(String password) {
		try {
			MessageDigest digest;
			digest = MessageDigest.getInstance("SHA-1");
			digest.reset();
			byte[] input;
			input = digest.digest(password.getBytes("UTF-8"));
			String p = "";
			for(byte e : input){
				p += String.valueOf(Character.toChars(e + 128)[0]) + "";
			}
			return p;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	 }
	
}
